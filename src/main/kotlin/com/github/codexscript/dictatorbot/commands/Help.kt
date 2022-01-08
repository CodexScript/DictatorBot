package com.github.codexscript.dictatorbot.commands

import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class Help : SlashCommand() {
    init {
        name = "help"
        help = "Get help on how to use the bot"
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }

        event.reply("""🇨🇳 ***${event.guild?.selfMember?.nickname ?: event.jda.selfUser.name} is here to serve the glorious Chinese Communist Party!*** 🇨🇳
            |
            |__Social Credit System__
            |The CCP has put in place a system to better evaluate the trustworthiness of citizens! It works like this:
            |> - Every citizen starts with a credit score of 1000
            |> - Your behavior is evaluated using numerous factors, which can impact your score positively or negatively
            |> - Your score is used to determine your social credit tier, which in turn determines your rights and privileges within the system
            |Use `/socialcredit` to see your current score and tier
            |Use `/tiers` to see the different tiers and their score ranges
            |
            |__Music__
            |`/play` to play music
            |`/skip` to skip the current song
            |`/clear` to clear the queue
            |`/queue` to see the current queue
            |`/volume` to change the volume
            |
            |__Other Commands__
            |Use `/soygrimes` to edit a given image to be behind the soy grimes picture
            |Use `/help` to see this message
        """.trimMargin()).setEphemeral(true).queue()
    }
}