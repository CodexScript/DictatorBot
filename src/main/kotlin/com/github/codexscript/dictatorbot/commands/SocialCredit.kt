package com.github.codexscript.dictatorbot.commands

import com.github.codexscript.dictatorbot.util.SocialCreditManager
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

class SocialCredit : SlashCommand() {
    init {
        name = "socialcredit"
        help = "Tells you your social credit"
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null || event.member == null) {
            return
        }

        event.deferReply().queue()
        val image = SocialCreditManager.createUserBanner(event.member!!)

        event.hook.sendFile(image, "socialcredit.png").queue()
    }
}