package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class ResetFilters : SlashCommand() {
    init {
        name = "resetfilters"
        help = "Resets all music filters"
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }

        if (!Bot.musicController.isLinkActive(event.guild!!)) {
            event.reply("Nothing is playing.").setEphemeral(true).queue()
            return
        }

        val filters = Bot.musicController.getGuildMusicManager(event.textChannel).link.player.filters
        filters.clear()
        filters.commit()

        event.reply("Filters reset").queue()
    }
}