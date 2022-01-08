package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.util.ConfigManager
import com.github.codexscript.dictatorbot.util.SocialCreditManager
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
            val config = ConfigManager.getConfigContent()
            val penalty = config.invalidCommandPenalty * -1
            event.reply("Nothing is playing. Waste of CCP resources. ${if (penalty < 0) penalty else "+$penalty"} social credit.").setEphemeral(true).queue()
            SocialCreditManager.addSocialCredit(event.member!!.id, penalty)
            return
        }

        event.reply("Skipped **${title}**").queue()

        Bot.musicController.getGuildMusicManager(event.textChannel).scheduler.nextTrack()
    }
}