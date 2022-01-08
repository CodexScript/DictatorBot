package com.github.codexscript.dictatorbot.events

import com.github.codexscript.dictatorbot.Bot
import com.github.codexscript.dictatorbot.util.ConfigManager
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URI

class ReadyEvent : ListenerAdapter() {

    private val LOG: Logger = LoggerFactory.getLogger(ReadyEvent::class.java)
    private val config = ConfigManager.getConfigContent()

    override fun onReady(event: ReadyEvent) {
        Bot.lavalink.setUserId(event.jda.selfUser.id)
        Bot.lavalink.addNode(URI("ws://${config.lavalinkIP}:${config.lavalinkPort}"), config.lavalinkPassword)
        LOG.info("${event.jda.selfUser.name} is now providing they/their services to the glorious CCP.")
    }
}