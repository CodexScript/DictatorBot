package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.models.WorkerOwnedSlashCommand
import lavalink.client.io.filters.Rotation
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class Rotate : WorkerOwnedSlashCommand() {
    init {
        name = "rotate"
        help = "Rotates the sound (8D AUDIO XXXTENTACION!!1!1one1)."
        options = listOf(OptionData(OptionType.NUMBER, "hz", "Rotation frequency", true))
        reward = 1
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }

        if (event.isAcknowledged || event.member == null) {
            return
        }

        if (!Bot.musicController.isLinkActive(event.guild!!)) {
            event.reply("Nothing is playing.").setEphemeral(true).queue()
            return
        }

        val hz = event.getOption("hz")?.asDouble

        if (hz == null) {
            event.reply("Please specify rotation frequency.").setEphemeral(true).queue()
            return
        }
        else if (hz < 0) {
            event.reply("Rotation frequency cannot be negative.").setEphemeral(true).queue()
            return
        }

        val link = Bot.musicController.getGuildMusicManager(event.textChannel).link

        if (link.player.filters.rotation == null) {
            link.player.filters.rotation = Rotation()
        }
        link.player.filters.rotation!!.frequency = hz.toFloat()
        link.player.filters.commit()

        event.reply("Set rotation to $hz Hz").queue()
        rewardScore(event, false)

    }
}