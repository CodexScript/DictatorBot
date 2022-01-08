package com.github.codexscript.dictatorbot.commands

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.codexscript.dictatorbot.models.SocialCreditTier
import com.github.codexscript.dictatorbot.models.WorkerOwnedSlashCommand
import com.github.codexscript.dictatorbot.models.config.TiktokConfig
import com.github.codexscript.dictatorbot.models.tiktok.TiktokFavsResponse
import com.github.codexscript.dictatorbot.models.tiktok.TiktokVideoListing
import com.github.codexscript.dictatorbot.util.ConfigManager
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.util.*
import kotlin.random.Random

class TiktokFavs : WorkerOwnedSlashCommand() {

    private var favs = mutableListOf<TiktokVideoListing>()

    private val LOG = LoggerFactory.getLogger(TiktokFavs::class.java)

    private var fetchTime = Date()

    init {
        name = "tiktokfavs"
        help = "Shows a random CCP-sponsored TikTok video."
        options = listOf(OptionData(OptionType.INTEGER, "index", "The index of the video to grab.", false))
        requiredTier = SocialCreditTier.AMINUS
        reward = 10
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }

        super.execute(event)

        if (event.isAcknowledged || event.member == null) {
            return
        }

        val config = ConfigManager.getConfigContent()

        if (config.tiktok == null) {
            event.reply("TikTok API key is not configured. Please contact the bot owner.").setEphemeral(true).queue()
            return
        }

        var waitMessage: Message? = null

        event.deferReply().queue()

        if (favs.isEmpty() || fetchTime.time + 21600000 < Date().time) {
            event.channel.sendMessage("Populating TikTok favorites list. This may take a minute...").queue {
                waitMessage = it
            }
            fetchFavorites(event.jda.httpClient, config.tiktok)
            waitMessage?.delete()?.queue()
        }

        val userIndex = event.getOption("index")?.asDouble?.toInt()

        var listing: TiktokVideoListing? = null
        var index = Int.MIN_VALUE

        if (userIndex == null) {
            var size = Int.MAX_VALUE

            while (size > 8000000) {
                index = Random.nextInt(0, favs.size)
                listing = favs[index]
                size = listing.video?.playAddr?.dataSize ?: 0
            }
        }
        else {
            index = userIndex
            if (index >= favs.size) {
                event.hook.editOriginal("Index out of bounds")
                return
            }
            if (index < 0) {
                index += favs.size + 1
                if (index < 0) {
                    event.hook.editOriginal("Index out of bounds")
                    return
                }
            }
            listing = favs[index]
        }

        if (listing?.video != null) {
            val video = listing.video!!.playAddr
            val videoCdnUrl = video?.urlList?.get(video.urlList.size - 1)

            if (videoCdnUrl != null) {
                val dlRequest = Request.Builder()
                    .url(videoCdnUrl)
                    .build()

                val bytes = event.jda.httpClient.newCall(dlRequest).execute().body?.bytes()

                if (bytes != null) {
                    LOG.info("Sending TikTok video ${listing.shareInfo.shareURL}")
                    val mapper = jacksonObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
                    LOG.debug(mapper.writeValueAsString(listing))
                    event.hook.editOriginal(bytes, "tiktok-$index.mp4").queue()
                    rewardScore(event, false)
                }
                else {
                    event.hook.editOriginal("Failed to download TikTok video.").queue()
                }
            }
            else {
                event.hook.editOriginal("Failed to download TikTok video.").queue()
            }
        }
        else {
            event.hook.editOriginal("Failed to download TikTok video.").queue()
        }
    }

    private fun isValidVideo(video: TiktokVideoListing, config: TiktokConfig): Boolean {
        var videoHashtagsValid = true
        var soundValid = true

        val badSounds = config.badSounds
        val badHashtags = config.badHashtags

        if (badHashtags != null && badHashtags.isNotEmpty() && video.textExtra != null) {

            videoHashtagsValid = !video.textExtra.any { it.hashtagName in badHashtags }
        }

        if (badSounds != null && badSounds.isNotEmpty()) {
            if (video.music?.id in badSounds) {
                soundValid = false
            }
        }

        return videoHashtagsValid && soundValid
    }

    private fun fetchFavorites(client: OkHttpClient, config: TiktokConfig) {
        LOG.info("Fetching TikTok favorites list")

        val mapper = jacksonObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).configure(JsonParser.Feature.IGNORE_UNDEFINED, true)

        fetchTime = Date()
        var cursor = 0
        var has_more = true

        var iterated = 0

        while(has_more && iterated < 500) {
            val url = HttpUrl.Builder()
                .scheme("https")
                .host("api19-normal-c-useast1a.tiktokv.com")
                .addPathSegments("aweme/v1/aweme/listcollection/")
                .addQueryParameter("app_language", "en")
                .addQueryParameter("count", "30")
                .addQueryParameter("cursor", cursor.toString())
                .addQueryParameter("account_region", "US")
                .build()

            val headers = Headers.Builder()
                .add("user-agent", "TikTok 16.6.5 rv:166515 (iPhone; iOS 14.0; en_US) Cronet")
                .add("x-khronos", config.xKhronos)
                .add("x-gorgon", config.xGorgon)
                .add("cookie", config.cookie)
                .build()

            val request = Request.Builder()
                .url(url)
                .headers(headers)
                .build()

            val response = client.newCall(request).execute()
            val body = response.body!!.string()
            try {
                val json: TiktokFavsResponse = mapper.readValue(body)
                val aweme_list = json.awemeList
                //favs.addAll(aweme_list)
                for (aweme in aweme_list) {
                    if (aweme.canPlay && isValidVideo(aweme, config)) {
                        favs.add(aweme)
                    }
                }
                has_more = json.hasMore == 1
                cursor = json.cursor
            }
            catch (e: Exception) {
                LOG.error(body)
                throw e
            }
            finally {
                response.close()
            }

            val diff = 500 - iterated
            if (diff < 30) {
                iterated += diff
            }
            else {
                iterated += 30
            }
        }
        LOG.info("Fetched ${favs.size} TikTok favorites")
    }
}