package com.github.codexscript.dictatorbot

import com.github.codexscript.dictatorbot.commands.*
import com.github.codexscript.dictatorbot.events.MessageEventSocialCredit
import com.github.codexscript.dictatorbot.events.ReadyEvent
import com.github.codexscript.dictatorbot.util.ConfigManager
import com.github.codexscript.dictatorbot.util.SocialCreditManager
import com.jagrosh.jdautilities.command.CommandClientBuilder
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Usage: java -jar DictatorBot.jar <bot-token>")
        return
    }

    val ownerId = ConfigManager.getConfigContent()["owner_id"] as String

    SocialCreditManager.ensureDatabase()
    SocialCreditManager.setSocialCredit("145639018955014154", 1000)

    val cmdClient = CommandClientBuilder()
        .setOwnerId(ownerId)
        .setActivity(Activity.streaming("BING CHILLING \uD83E\uDD76 \uD83C\uDF66", "https://www.youtube.com/watch?v=KH_XIt-hm2Y"))
        .addSlashCommands(
            Help(),
            SoyGrimes(),
            SocialCredit(),
            ListSocialCreditTiers(),
            SocialCreditMod()
        )
        .forceGuildOnly("272896244412579841")
        .build()

    val jda = JDABuilder.createDefault(args[0])
        .addEventListeners(cmdClient, ReadyEvent(), MessageEventSocialCredit())
        .build()
}