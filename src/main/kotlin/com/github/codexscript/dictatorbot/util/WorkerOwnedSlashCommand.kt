package com.github.codexscript.dictatorbot.util

import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent

abstract class WorkerOwnedSlashCommand : SlashCommand() {
    protected var rewardScore: Int = 0
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

        if (rewardScore != 0) {
            val socialCredit = SocialCreditManager.getSocialCredit(event.user.id)
            SocialCreditManager.setSocialCredit(event, listOf("Used ${this.name} command (${if (rewardScore >= 0) "+$rewardScore" else rewardScore})"), socialCredit + rewardScore)
        }
    }
}