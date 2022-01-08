package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.models.WorkerOwnedSlashCommand
import lavalink.client.io.filters.Distortion
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class BassBoost : WorkerOwnedSlashCommand() {
    init {
        name = "bassboost"
        help = "Distort the bass like a bad 2016 meme."
        options = listOf(OptionData(OptionType.NUMBER, "boost", "The bass percentage (positive #, 0 to reset)", true))
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

        val boost = event.getOption("boost")?.asDouble

        if (boost == null) {
            event.reply("Please specify a boost percentage.").setEphemeral(true).queue()
            return
        }
        else if (boost < 0) {
            event.reply("Boost cannot be negative.").setEphemeral(true).queue()
            return
        }

        if (!Bot.musicController.isLinkActive(event.guild!!)) {
            event.reply("Nothing is playing.").setEphemeral(true).queue()
            return
        }

        val link = Bot.musicController.getGuildMusicManager(event.textChannel).link

        //for (i in 0..14) {
        //    link.player.filters.bands[i] = boost!!.div(400f)
        //}


        if (link.player.filters.distortion == null) {
            link.player.filters.distortion = Distortion()
        }
        link.player.filters.distortion!!.sinScale = boost.toFloat()
        link.player.filters.distortion!!.cosScale = boost.toFloat()


        link.player.filters.commit()


        event.reply("Bass boost set to +$boost%").queue()
        rewardScore(event, false)
    }
}