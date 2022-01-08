package com.github.codexscript.dictatorbot.commands

import com.github.codexscript.dictatorbot.util.SocialCreditManager
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import kotlin.concurrent.thread

class SocialCredit : SlashCommand() {
    init {
        name = "socialcredit"
        help = "Tells you your social credit"
        options = listOf(OptionData(OptionType.USER, "user", "The user to check the social credit of", false))
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null || event.member == null) {
            return
        }

        if (event.guild == null) {
            event.reply("You can't use that command here.").setEphemeral(true).queue()
            return
        }

        if (event.guild?.selfMember?.permissions?.contains(Permission.MESSAGE_READ) == false
            || event.guild?.selfMember?.permissions?.contains(Permission.MESSAGE_READ) == null) {

            event.reply("I don't have permission to read messages, therefore this province cannot partake in the social credit system.").queue()
            return
        }

        val specifiedTarget = event.getOption("user")?.asMember

        var user: Member?

        if (specifiedTarget != null) {
            user = specifiedTarget
        } else {
            user = event.member
        }

        event.deferReply().setEphemeral(true).queue()
        thread(start = true) {
            val image = SocialCreditManager.createUserBanner(user!!)

            event.hook.sendFile(image, "socialcredit.png").queue()
        }
    }
}