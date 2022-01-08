package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class Skip : SlashCommand() {
    init {
        name = "skip"
        help = "Skips the current song."
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null || event.guild == null) {
            return
        }

        event.reply("Skipped **${Bot.musicController.getGuildMusicManager(event.textChannel).scheduler.currentTrack?.info?.title}**").queue()

        Bot.musicController.getGuildMusicManager(event.textChannel).scheduler.nextTrack()
    }
}