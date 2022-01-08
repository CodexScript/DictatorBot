package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.models.WorkerOwnedSlashCommand
import com.github.codexscript.dictatorbot.util.ConfigManager
import com.github.codexscript.dictatorbot.util.SocialCreditManager
import lavalink.client.io.filters.Timescale
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class Pitch : WorkerOwnedSlashCommand() {
    init {
        name = "pitch"
        help = "Changes the pitch of the music."
        options = listOf(OptionData(OptionType.INTEGER, "pitch", "The new pitch, in percentage", true))
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

        val pitch = event.getOption("pitch")?.asLong
        val realPitch = pitch?.div(100f)

        val link = Bot.lavalink.getLink(event.guild)

        if (pitch != null && realPitch != null) {
            if (link.player.filters.timescale == null) {
                link.player.filters.timescale = Timescale()
            }
            link.player.filters.timescale?.pitch = realPitch
            link.player.filters.commit()

            rewardScore(event, false)
            event.reply("Pitch set to $pitch%.").queue()
        }
    }
}