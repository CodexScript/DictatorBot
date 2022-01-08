package com.github.codexscript.dictatorbot.music

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import lavalink.client.io.jda.JdaLink
import lavalink.client.player.event.IPlayerEventListener
import lavalink.client.player.event.PlayerEvent
import lavalink.client.player.event.TrackEndEvent
import lavalink.client.player.event.TrackStartEvent
import net.dv8tion.jda.api.entities.TextChannel
import org.slf4j.LoggerFactory
import java.util.concurrent.LinkedBlockingQueue

class TrackScheduler(val link: JdaLink, val channel: TextChannel) : IPlayerEventListener {
    private val LOG = LoggerFactory.getLogger(TrackScheduler::class.java)
    val queue = LinkedBlockingQueue<AudioTrack>()
    var currentTrack: AudioTrack? = null
    private var shouldAnnounce = false

    fun queue(track: AudioTrack) {
        if (currentTrack == null) {
            link.player.playTrack(track)
        }
        else {
            queue.offer(track)
        }
    }

    fun nextTrack() {
        link.player.stopTrack()
        if (queue.size == 0) {
            link.destroy()
            currentTrack = null
        }
        else {
            link.player.playTrack(queue.poll())
        }
    }

    fun clearQueue() {
        queue.clear()
    }

    override fun onEvent(event: PlayerEvent?) {
        if (event is TrackStartEvent) {
            LOG.debug("Now playing: " + event.track.info.title)
            if (shouldAnnounce) {
                channel.sendMessage("Now playing: **${event.track.info.title}**").queue()
            }
            else {
                shouldAnnounce = true
            }
            currentTrack = event.track
        }
        else if (event is TrackEndEvent) {
            if (event.reason.mayStartNext) {
                nextTrack()
            }
        }
    }
}