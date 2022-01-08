package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.models.SocialCreditTier
import com.github.codexscript.dictatorbot.models.WorkerOwnedSlashCommand
import com.github.codexscript.dictatorbot.util.ConfigManager
import com.github.codexscript.dictatorbot.util.SocialCreditManager
import lavalink.client.io.filters.*
import lavalink.client.io.filters.Vibrato
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import kotlin.random.Random

class Demons : WorkerOwnedSlashCommand() {
    init {
        name = "demons"
        help = "Sets all filters to random values."
        requiredTier = SocialCreditTier.APLUS
        reward = 1
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }

        if (event.isAcknowledged || event.member == null) {
            return
        }

        if (!Bot.musicController.isLinkActive(event.guild!!)) {
            val config = ConfigManager.getConfigContent()
            val penalty = config.invalidCommandPenalty * -1
            event.reply("Nothing is playing. Waste of CCP resources. ${if (penalty < 0) penalty else "+$penalty"} social credit.").setEphemeral(true).queue()
            SocialCreditManager.addSocialCredit(event.member!!.id, penalty)
            return
        }

        val link = Bot.musicController.getGuildMusicManager(event.textChannel).link

        if (link.player.filters.karaoke == null) {
            link.player.filters.karaoke = Karaoke()
        }
        link.player.filters.karaoke!!.level = Random.nextFloat() * 5

        if (link.player.filters.timescale == null) {
            link.player.filters.timescale = Timescale()
        }
        link.player.filters.timescale!!.speed = 0.1f + Random.nextFloat() * (2 - 0.1f)
        link.player.filters.timescale!!.pitch = 0.1f + Random.nextFloat() * (2 - 0.1f)

        if (link.player.filters.tremolo == null) {
            link.player.filters.tremolo = Tremolo()
        }
        link.player.filters.tremolo!!.frequency = Random.nextFloat() * 3
        link.player.filters.tremolo!!.depth = Random.nextFloat()

        if (link.player.filters.vibrato == null) {
            link.player.filters.vibrato = Vibrato()
        }
        link.player.filters.vibrato!!.frequency = Random.nextFloat() * 10
        link.player.filters.vibrato!!.depth = Random.nextFloat()

        if (link.player.filters.distortion == null) {
            link.player.filters.distortion = Distortion()
        }
        link.player.filters.distortion!!.sinOffset = Random.nextFloat() * 3
        link.player.filters.distortion!!.cosOffset = Random.nextFloat() * 3

        link.player.filters.commit()

        event.reply("""Demons have been summoned.
            |```
            |Karaoke level: ${link.player.filters.karaoke!!.level}
            |Timescale speed: ${link.player.filters.timescale!!.speed}
            |Timescale pitch: ${link.player.filters.timescale!!.pitch}
            |Tremolo frequency: ${link.player.filters.tremolo!!.frequency}
            |Tremolo depth: ${link.player.filters.tremolo!!.depth}
            |Vibrato frequency: ${link.player.filters.vibrato!!.frequency}
            |Vibrato depth: ${link.player.filters.vibrato!!.depth}
            |Distortion sin offset: ${link.player.filters.distortion!!.sinOffset}
            |Distortion cos offset: ${link.player.filters.distortion!!.cosOffset}
            |```
            |""".trimMargin()).queue()
        rewardScore(event, false)

    }
}