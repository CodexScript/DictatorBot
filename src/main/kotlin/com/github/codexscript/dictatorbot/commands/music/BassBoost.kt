package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.models.WorkerOwnedSlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class BassBoost : WorkerOwnedSlashCommand() {
    init {
        name = "bassboost"
        help = "Distort the bass like a bad 2016 meme."
        options = listOf(OptionData(OptionType.INTEGER, "boost", "The bass percentage (-100 to 400)", true))
        reward = 1
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }

        val boost = event.getOption("boost")?.asLong

        val link = Bot.lavalink.getLink(event.guild)

        for (i in 0..14) {
            link.player.filters.bands[i] = boost!!.div(400f)
        }

        link.player.filters.commit()
        rewardScore(event, false)

        event.reply("Bass boost set to $boost%").queue()
    }
}