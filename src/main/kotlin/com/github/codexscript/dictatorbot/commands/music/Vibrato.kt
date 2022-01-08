package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.models.WorkerOwnedSlashCommand
import lavalink.client.io.filters.Vibrato
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class Vibrato : WorkerOwnedSlashCommand() {
    init {
        name = "vibrato"
        help = "Vibrato the music"
        options = listOf(OptionData(OptionType.NUMBER, "frequency", "Frequency of vibrato", true))
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

        val frequency = event.getOption("frequency")?.asDouble

        if (frequency == null) {
            event.reply("Please specify vibrato frequency.").setEphemeral(true).queue()
            return
        }
        else if (frequency <= 0 || frequency > 14) {
            event.reply("Vibrato frequency must be 0 < x <= 14.").setEphemeral(true).queue()
            return
        }

        val link = Bot.musicController.getGuildMusicManager(event.textChannel).link

        if (link.player.filters.vibrato == null) {
            link.player.filters.vibrato = Vibrato()
        }

        link.player.filters.vibrato!!.frequency = frequency.toFloat()
        link.player.filters.vibrato!!.depth = 1f
        link.player.filters.commit()

        event.reply("Vibrato frequency set to $frequency.").queue()
        rewardScore(event, false)

    }
}