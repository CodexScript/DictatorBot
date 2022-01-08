package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.models.WorkerOwnedSlashCommand
import lavalink.client.io.filters.Distortion
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import org.slf4j.LoggerFactory

class Play : WorkerOwnedSlashCommand() {
    private val LOG = LoggerFactory.getLogger(Play::class.java)
    init {
        name = "play"
        help = "Plays or queue a song"
        options = listOf(OptionData(OptionType.STRING, "query","The URL or search term", true))
        reward = 1
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }

        super.execute(event)

        if (event.isAcknowledged || event.member == null) {
            return
        }

        val query = event.getOption("query")?.asString

        if (query != null) {
            LOG.debug("Query: $query")
            Bot.musicController.loadAndPlay(query, event)
            rewardScore(event, false)
        }
    }
}