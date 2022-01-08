package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent


class Clear : SlashCommand() {
    init {
        name = "clear"
        help = "Clears the queue."
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }
        Bot.musicController.clearQueue(event)
    }
}