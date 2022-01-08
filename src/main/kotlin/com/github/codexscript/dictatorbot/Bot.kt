package com.github.codexscript.dictatorbot

import com.github.codexscript.dictatorbot.commands.*
import com.github.codexscript.dictatorbot.commands.music.*
import com.github.codexscript.dictatorbot.events.MessageEventSocialCredit
import com.github.codexscript.dictatorbot.events.ReadyEvent
import com.github.codexscript.dictatorbot.music.MusicController
import com.github.codexscript.dictatorbot.util.ConfigManager
import com.github.codexscript.dictatorbot.util.SocialCreditManager
import com.jagrosh.jdautilities.command.CommandClientBuilder
import lavalink.client.io.jda.JdaLavalink
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity

class Bot(token: String)  {
    companion object {
        private val config = ConfigManager.getConfigContent()
        private lateinit var jda: JDA
        val musicController = MusicController()
        val lavalink = JdaLavalink(1) { jda }
    }


    init {
        SocialCreditManager.ensureDatabase()
        val cmdClient = CommandClientBuilder()
            .setOwnerId(config.ownerID)
            .setActivity(Activity.streaming("BING CHILLING \uD83E\uDD76 \uD83C\uDF66", "https://www.youtube.com/watch?v=KH_XIt-hm2Y"))
            .addSlashCommands(
                Help(),
                SoyGrimes(),
                SocialCredit(),
                ListSocialCreditTiers(),
                SocialCreditMod(),
                Solve(),
                Play(),
                Skip(),
                Clear(),
                Queue(),
                Volume(),
                Pause(),
                Resume(),
                Speed(),
                Pitch(),
                BassBoost(),
                Rotate(),
                Vibrato(),
                Demons(),
                Stop(),
                ResetFilters(),
                TiktokFavs(),
                Source()
            )
            .forceGuildOnly("272896244412579841")
            .build()


         jda = JDABuilder.createDefault(token)
            .addEventListeners(cmdClient, ReadyEvent(), MessageEventSocialCredit(), lavalink)
             .setVoiceDispatchInterceptor(lavalink.voiceInterceptor)
            .build()
    }
}

fun main(args: Array<String>) {

    //System.setProperty("java.io.tmpdir", "tmp")

    val token = System.getenv("BOT_TOKEN")
    if (token == null) {
        println("Please set BOT_TOKEN environment variable")
        return
    }

    Bot(token)
}