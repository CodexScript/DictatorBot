package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class Pause : SlashCommand() {
    init {
        name = "pause"
        help = "Pauses the current song."
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }

        val link = Bot.lavalink.getLink(event.guild)
        link.player.isPaused = true
        event.reply("Paused.").queue()
    }
}