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

        val title = Bot.musicController.getGuildMusicManager(event.textChannel).scheduler.currentTrack?.info?.title

        if (title == null) {
            event.reply("Nothing is playing.").setEphemeral(true).queue()
            return
        }

        event.reply("Skipped **${title}**").queue()

        Bot.musicController.getGuildMusicManager(event.textChannel).scheduler.nextTrack()
    }
}