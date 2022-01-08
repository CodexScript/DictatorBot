package com.github.codexscript.dictatorbot.music

import com.github.codexscript.dictatorbot.Bot
import net.dv8tion.jda.api.entities.TextChannel

class GuildMusicManager(val guildMusicChannel: TextChannel) {

    var link = Bot.lavalink.getLink(guildMusicChannel.guild)
    var scheduler: TrackScheduler = TrackScheduler(link, guildMusicChannel)

    init {
        link.player.addListener(scheduler)
    }

}