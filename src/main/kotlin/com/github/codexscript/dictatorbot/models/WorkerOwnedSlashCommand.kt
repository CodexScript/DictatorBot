package com.github.codexscript.dictatorbot.models

import com.github.codexscript.dictatorbot.util.SocialCreditManager
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

/**
 * A [SlashCommand] that can have rewards and constraints relating to the social credit system.
 *
 * @property[reward] The amount of social credit to give to the user upon successful execution of the command.
 * @property[requiredTier] The minimum required [SocialCreditTier] of the user to use the command.
 */

abstract class WorkerOwnedSlashCommand : SlashCommand() {
    protected var reward: Int = 0
    protected var requiredTier: SocialCreditTier? = null

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }
        if (requiredTier != null) {
            val tier = SocialCreditManager.getSocialCreditTier(SocialCreditManager.getSocialCredit(event.user.id))
            if (tier < requiredTier!!) {
                event.reply("""You need to have a social credit tier of at least a $requiredTier to use this command.
                |Your current social credit tier: $tier""".trimMargin()).setEphemeral(true).queue()
                return
            }
        }
    }

    fun rewardScore(event: SlashCommandEvent?, reply: Boolean = true) {
        if (event == null) {
            return
        }

        if (this.reward != 0) {
            if (reply) {
                val socialCredit = SocialCreditManager.getSocialCredit(event.user.id)

                SocialCreditManager.setSocialCredit(
                    event,
                    listOf("Used ${this.name} command (${if (this.reward >= 0) "+$this.reward" else this.reward})"),
                    socialCredit + this.reward,
                    false
                )
            } else {
                SocialCreditManager.addSocialCredit(event.user.id, this.reward)
            }
        }
    }
}