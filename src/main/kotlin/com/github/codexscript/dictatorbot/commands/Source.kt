package com.github.codexscript.dictatorbot.commands

import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class Source : SlashCommand() {
    init {
        name = "source"
        help = "Links to the source code of the bot."
    }

    override fun execute(event: SlashCommandEvent?) {
        event?.reply("\uD83C\uDDE8\uD83C\uDDF3 In glorious China, the workers own all means of production. That includes me! Having ownership over me means you may view my source code, found here: " +
                "https://github.com/CodexScript/DictatorBot")?.setEphemeral(true)?.queue()
    }


}