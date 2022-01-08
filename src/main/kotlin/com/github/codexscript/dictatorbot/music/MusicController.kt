package com.github.codexscript.dictatorbot.music

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import lavalink.client.io.Link
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import java.net.URI

class MusicController {

    private val playerManager: AudioPlayerManager = DefaultAudioPlayerManager()
    private val musicManagers = mutableMapOf<Long, GuildMusicManager>()

    init {
        AudioSourceManagers.registerRemoteSources(playerManager)
    }

    fun loadAndPlay(url: String, event: SlashCommandEvent) {
        if (event.guild == null) {
            event.reply("You can only use this command in a server.").queue()
            return
        }

        if (event.member?.voiceState?.channel == null) {
            event.reply("You are not connected to a voice channel.").queue()
            return
        }
        event.deferReply().queue()
        val manager = getGuildMusicManager(event.textChannel)
        var identifier = url
        try {
            val uri = URI(url)
            if (!(uri.host == "youtu.be" || uri.host == "www.youtube.com")) {
                identifier = "ytsearch:$url"
            }
        }
        catch (e: Exception) {
            identifier = "ytsearch:$url"
        }
        manager.link.connect(event.member!!.voiceState!!.channel!!)
        playerManager.loadItem(identifier, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                if (manager.scheduler.currentTrack == null) {
                    event.hook.editOriginal("Now playing: **${track.info.title}**").queue()
                }
                else {
                    event.hook.editOriginal("Added to queue: **${track.info.title}**").queue()
                }
                manager.scheduler.queue(track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                if (playlist.isSearchResult) {
                    trackLoaded(playlist.tracks[0])
                    return
                }
                for (track in playlist.tracks) {
                    manager.scheduler.queue(track)
                }
                event.hook.editOriginal("Queued ${playlist.tracks.size} tracks.").queue()
            }

            override fun noMatches() {
                event.hook.editOriginal("No matches found.").queue()
                manager.link.destroy()
            }

            override fun loadFailed(exception: FriendlyException) {
                event.hook.editOriginal("Could not play: " + exception.message).queue()
                manager.link.destroy()
            }
        })
    }

    fun clearQueue(event: SlashCommandEvent) {
        val manager = getGuildMusicManager(event.textChannel)
        manager.scheduler.clearQueue()
        event.reply("Queue cleared.").queue()
    }

    fun getGuildMusicManager(guildMusicChannel: TextChannel) : GuildMusicManager {
        val guildId = guildMusicChannel.guild.idLong
        var musicManager = musicManagers[guildId]
        if ((musicManager == null) || ((musicManager.link.state == Link.State.DESTROYED) || (musicManager.link.state == Link.State.DESTROYING))) {
            musicManager = GuildMusicManager(guildMusicChannel)
            musicManagers[guildId] = musicManager
        }

        return musicManager
    }
}