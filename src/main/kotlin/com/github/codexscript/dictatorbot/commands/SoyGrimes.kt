package com.github.codexscript.dictatorbot.commands

import com.github.codexscript.dictatorbot.util.SocialCreditManager
import com.github.codexscript.dictatorbot.util.SocialCreditTier
import com.github.codexscript.dictatorbot.util.WorkerOwnedSlashCommand
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import okhttp3.Request
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.net.URL
import javax.imageio.ImageIO

class SoyGrimes : WorkerOwnedSlashCommand() {
    init {
        name = "soygrimes"
        help = "Edits a given image to be behind the soy grimes picture"
        options = listOf(OptionData(OptionType.STRING, "url", "The URL to the background image."))
        requiredTier = SocialCreditTier.AMINUS
    }

    override fun execute(event: SlashCommandEvent?) {
        if (event == null) {
            return
        }
        super.execute(event)

        // Safeguard because "return" in super method does not stop execution here
        if (event.isAcknowledged || event.member == null) {
            return
        }

        val url = event.getOption("url")?.asString
        if (url == null) {
            event.reply("You must provide a URL to the background image.").queue()
            return
        }

        event.deferReply().queue()

        val urlRequest = Request.Builder()
            .url(URL(url))
            .build()

        val response = event.jda.httpClient.newCall(urlRequest).execute()

        if (!response.isSuccessful) {
            event.reply("Could not load image.").queue()
            return
        }

        val imageBytes = response.body?.byteStream()

        if (imageBytes == null) {
            event.reply("Could not load image.").queue()
            return
        }

        val image = ImageIO.read(imageBytes)

        val soyTemplate = SoyGrimes::class.java.getResource("/picedit/grimes-soyjack.png")

        val soyjack = ImageIO.read(soyTemplate)

        val tmpImg = image.getScaledInstance(soyjack.width, soyjack.height, java.awt.Image.SCALE_SMOOTH)

        val bi = BufferedImage(soyjack.width, soyjack.height, BufferedImage.TYPE_INT_ARGB)
        val bg = bi.createGraphics()

        bg.drawImage(tmpImg, 0, 0, null)
        bg.dispose()

        val graphics = bi.createGraphics()

        graphics.drawImage(soyjack, 0, 0, null)

        graphics.dispose()

        val bytes = ByteArrayOutputStream()
        ImageIO.write(bi, "png", bytes)

        event.hook.sendFile(bytes.toByteArray(), "soygrimes.png").queue()
    }
}