package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class Volume : SlashCommand() {
    init {
        name = "volume"
        help = "Changes the volume of the music player."
        options = listOf(OptionData(OptionType.INTEGER, "volume", "The new volume level, in percentage.", true))
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }
        val volume = event.getOption("volume")?.asLong
        val link = Bot.lavalink.getLink(event.guild)
        if (volume != null) {
            if (volume <= 0 || volume > 500) {
                event.reply("Volume must be 0 < vol <= 500.").setEphemeral(true).queue()
                return
            }
            link.player.volume = volume.toInt()
            event.reply("Volume set to $volume%.").queue()
        }
    }
}