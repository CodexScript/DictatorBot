package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.models.WorkerOwnedSlashCommand
import lavalink.client.io.filters.Timescale
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class Speed : WorkerOwnedSlashCommand() {
    init {
        name = "speed"
        help = "Changes the playback speed."
        options = listOf(OptionData(OptionType.INTEGER, "speed", "The new speed, in percentage", true))
        reward = 1
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }

        if (!Bot.musicController.isLinkActive(event.guild!!)) {
            event.reply("Nothing is playing.").setEphemeral(true).queue()
            return
        }

        val speed = event.getOption("speed")?.asLong
        val realSpeed = speed?.div(100f)
        val link = Bot.lavalink.getLink(event.guild)
        if (speed != null && realSpeed != null) {
            if (link.player.filters.timescale == null) {
                link.player.filters.timescale = Timescale()
            }
            link.player.filters.timescale?.speed = realSpeed
            link.player.filters.commit()

            rewardScore(event, false)
            event.reply("Speed set to $speed%.").queue()
        }
    }


}