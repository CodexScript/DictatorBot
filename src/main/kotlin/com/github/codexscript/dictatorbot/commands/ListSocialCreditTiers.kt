package com.github.codexscript.dictatorbot.commands

import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class ListSocialCreditTiers : SlashCommand() {
    init {
        name = "tiers"
        help = "Lists the social credit tiers"
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }

        event.reply("""🇨🇳 Social Credit tiers:
            |1050+ --> AAA
            |1030-1049 --> AA
            |1007-1029 --> A+
            |984-1006 --> A
            |960-983 --> A-
            |850-959 --> B
            |600-849 --> C
            |0-599 --> D
        """.trimMargin()).setEphemeral(true).queue()
    }
}