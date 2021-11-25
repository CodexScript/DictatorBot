package com.github.codexscript.dictatorbot.commands

import com.github.codexscript.dictatorbot.util.SocialCreditManager
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

class SocialCreditMod : SlashCommand() {
    init {
        name = "socialcredit_mod"
        help = "Only President Xi may use this command. You have been warned!"
        ownerCommand = true
        defaultEnabled = false
        children = arrayOf(SetSocialCredit(), AddSocialCredit())
    }

    override fun execute(event: SlashCommandEvent?) {
    }

    companion object {
        private class SetSocialCredit : SlashCommand() {
            init {
                name = "set"
                help = "Sets the users social credit to the provided value"
                options = listOf(OptionData(OptionType.STRING, "user", "The users ID", true),
                        OptionData(OptionType.INTEGER, "credit", "The amount", true))
            }

            override fun execute(event: SlashCommandEvent?) {
                if (event == null) {
                    return
                }

                if (!event.member?.isOwner!!) {

                    return
                }

                val user = event.getOption("user")
                val credit = event.getOption("credit")

                if (user == null || credit == null) {
                    event.reply("Please provide a user and a credit amount").setEphemeral(true).queue()
                    return
                }

                SocialCreditManager.setSocialCredit(user.asString, credit.asDouble.toInt())
                event.reply("Set social credit for ${user.asString} to ${credit.asDouble.toInt()}").setEphemeral(true).queue()
            }
        }

        private class AddSocialCredit : SlashCommand() {
            init {
                name = "add"
                help = "Adds provided amount to the users social credit."
                options = listOf(OptionData(OptionType.STRING, "user", "The ID user", true),
                        OptionData(OptionType.INTEGER, "credit", "The amount", true))
            }

            override fun execute(event: SlashCommandEvent?) {
                if (event == null) {
                    return
                }

                val user = event.getOption("user")
                val credit = event.getOption("credit")

                if (user == null || credit == null) {
                    event.reply("Please provide a user and a credit amount").setEphemeral(true).queue()
                    return
                }

                val oldCredit = SocialCreditManager.getSocialCredit(user.asString)

                SocialCreditManager.setSocialCredit(user.asString, oldCredit + credit.asDouble.toInt())
                event.reply("Set social credit for ${user.asString} to ${oldCredit + credit.asDouble.toInt()}").setEphemeral(true).queue()
            }
        }
    }
}