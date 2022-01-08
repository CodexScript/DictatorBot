package com.github.codexscript.dictatorbot.commands.music

import com.github.codexscript.dictatorbot.Bot
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class Queue : SlashCommand() {
    init {
        name = "queue"
        help = "Shows the current queue"
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null || event.guild == null) {
            return
        }

        val queue = Bot.musicController.getGuildMusicManager(event.textChannel).scheduler.queue
        if (queue.isEmpty()) {
            event.reply("Queue is empty").setEphemeral(true).queue()
        } else {
            val builder = StringBuilder()
            builder.append("```\n")
            queue.forEachIndexed { index, track ->
                builder.append("${index + 1}. ${track.info.title}\n")
            }
            builder.append("```\n")
            event.reply(builder.toString()).setEphemeral(true).queue()
        }
    }
}