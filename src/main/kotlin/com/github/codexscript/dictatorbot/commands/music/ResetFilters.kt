package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.util.ConfigManager
import com.github.codexscript.dictatorbot.util.SocialCreditManager
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
            val config = ConfigManager.getConfigContent()
            val penalty = config.invalidCommandPenalty * -1
            event.reply("Nothing is playing. Waste of CCP resources. ${if (penalty < 0) penalty else "+$penalty"} social credit.").setEphemeral(true).queue()
            SocialCreditManager.addSocialCredit(event.member!!.id, penalty)
            return
        }

        val filters = Bot.musicController.getGuildMusicManager(event.textChannel).link.player.filters
        filters.clear()
        filters.commit()

        event.reply("Filters reset").queue()
    }
}