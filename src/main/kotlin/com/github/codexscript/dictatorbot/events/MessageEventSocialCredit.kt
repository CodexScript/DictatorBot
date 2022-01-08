package com.github.codexscript.dictatorbot.events

import com.github.codexscript.dictatorbot.util.ConfigManager
import com.github.codexscript.dictatorbot.util.SocialCreditManager
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.LoggerFactory
import kotlin.random.Random

class MessageEventSocialCredit : ListenerAdapter() {
    private val LOG = LoggerFactory.getLogger(MessageEventSocialCredit::class.java)
    private val phrases: Map<String, Int> = ConfigManager.getConfigContent().socialCreditPhrases
    private val phrasesRegex: Map<String, Int> = ConfigManager.getConfigContent().socialCreditRegex
    override fun onMessageReceived(event: MessageReceivedEvent) {
        super.onMessageReceived(event)

        if (event.message.author.isBot) return
        LOG.debug("${event.author.asTag} in ${event.guild.name}: ${event.message.contentRaw}")

        val socialCredit = SocialCreditManager.getSocialCredit(event.author.id)
        var socialCreditNew = socialCredit
        val reasons = mutableListOf<String>()

        phrases.forEach {
            if (event.message.contentRaw.lowercase().contains(it.key.lowercase())) {
                socialCreditNew += it.value
                reasons.add("${it.key} (${if (it.value >= 0) "+" + it.value else it.value})")
            }
        }

        phrasesRegex.forEach {
            val regex = Regex(it.key, RegexOption.IGNORE_CASE)
            val match = regex.find(event.message.contentRaw)
            if (match != null) {
                socialCreditNew += it.value
                reasons.add("${match.value} (${if (it.value >= 0) "+" + it.value else it.value})")
            }
        }

        if (socialCreditNew != socialCredit) {
            LOG.debug("${event.author.name} (${event.author.id}) social credit: $socialCredit -> $socialCreditNew")
            SocialCreditManager.setSocialCredit(event, reasons, socialCreditNew)
        }
        else {
            val dice = Random.nextInt(20)
            if (dice == 0) {
                socialCreditNew = socialCredit + 1
                reasons.clear()
                reasons.add("Activity")
                LOG.debug("${event.author.name} (${event.author.id}) social credit: $socialCredit -> $socialCreditNew")
                SocialCreditManager.setSocialCredit(event, reasons, socialCreditNew, false)
            }
        }
    }
}