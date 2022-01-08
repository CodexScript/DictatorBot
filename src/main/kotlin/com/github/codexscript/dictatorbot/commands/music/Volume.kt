package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.util.ConfigManager
import com.github.codexscript.dictatorbot.util.SocialCreditManager
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class Volume : SlashCommand() {
    init {
        name = "volume"
        help = "Changes the volume of the music player."
        options = listOf(OptionData(OptionType.INTEGER, "volume", "The new volume level, in percentage", true))
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }

        if (!Bot.musicController.isLinkActive(event.guild!!)) {
            val config = ConfigManager.getConfigContent()
            val penalty = config.invalidCommandPenalty * -1
            event.reply("Nothing is playing. Waste of CCP resources. ${if (penalty < 0) penalty else "+$penalty"} social credit.").setEphemeral(true).queue()
            SocialCreditManager.addSocialCredit(event.member!!.id, penalty)
            return
        }

        val volume = event.getOption("volume")?.asLong
        val link = Bot.lavalink.getLink(event.guild)
        if (volume != null) {
            if (volume <= 0 || volume > 1000) {
                event.reply("Volume must be 0 < vol <= 1000.").setEphemeral(true).queue()
                return
            }
            link.player.volume = volume.toInt()
            event.reply("Volume set to $volume%.").queue()
        }
    }
}