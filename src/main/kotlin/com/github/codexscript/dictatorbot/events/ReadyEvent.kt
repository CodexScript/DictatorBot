package com.github.codexscript.dictatorbot.events

import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ReadyEvent : ListenerAdapter() {

    private val LOG: Logger = LoggerFactory.getLogger(ReadyEvent::class.java)

    override fun onReady(event: ReadyEvent) {
        super.onReady(event)
        LOG.info("John Xina is now providing his services to the glorious CCP.")
    }
}