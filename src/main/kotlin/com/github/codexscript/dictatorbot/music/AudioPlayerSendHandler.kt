package com.github.codexscript.dictatorbot.music

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler
import java.nio.ByteBuffer

class AudioPlayerSendHandler(private var player: AudioPlayer) : AudioSendHandler {

    private lateinit var lastFrame: AudioFrame

    override fun canProvide(): Boolean {
        lastFrame = player.provide()
        return lastFrame != null
    }

    override fun provide20MsAudio(): ByteBuffer? {
        return ByteBuffer.wrap(lastFrame.data)
    }

    override fun isOpus(): Boolean {
        return true
    }

}