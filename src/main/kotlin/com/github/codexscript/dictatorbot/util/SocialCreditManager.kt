package com.github.codexscript.dictatorbot.util

import com.github.codexscript.dictatorbot.models.SocialCreditTier
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.awt.Font
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.net.URL
import java.sql.Connection
import java.sql.DriverManager
import javax.imageio.ImageIO

class SocialCreditManager {
    companion object {
        private val font = Font.createFont(Font.TRUETYPE_FONT, SocialCreditManager::class.java.getResourceAsStream("/font/seven-monkey-fury-bb.regular.ttf")).deriveFont(Font.PLAIN, 72f)
        private val LOG = LoggerFactory.getLogger(SocialCreditManager::class.java)

        private val connection = DriverManager.getConnection("jdbc:sqlite:data/social_credit.db")

        fun ensureDatabase() {
            LOG.debug("Ensuring database")
            val sql = "CREATE TABLE IF NOT EXISTS social_credit (user_id TINYTEXT PRIMARY KEY, social_credit INTEGER NOT NULL DEFAULT 1000)"

            val stmt = connection.createStatement()

            stmt.execute(sql)
        }

        fun getSocialCredit(userId: String): Int {
            val sql = "SELECT social_credit FROM social_credit WHERE user_id = ?"

            val stmt = connection.prepareStatement(sql)

            stmt.setString(1, userId)

            val rs = stmt.executeQuery()

            if (rs.next()) {
                return rs.getInt("social_credit")
            }
            setSocialCredit(userId, 1000)
            return 1000
        }

        fun setSocialCredit(userId: String, socialCredit: Int) {
            val sql = "INSERT OR REPLACE INTO social_credit (user_id, social_credit) VALUES (?, ?)"

            val stmt = connection.prepareStatement(sql)

            stmt.setString(1, userId)
            stmt.setInt(2, if (socialCredit > 0) socialCredit else 0)

            stmt.execute()
        }

        fun setSocialCredit(event: GenericInteractionCreateEvent, reasons: List<String>, socialCredit: Int, reply: Boolean = true) {
            setSocialCredit(event.user.id, socialCredit)

            var reasonString = "Your new social credit score: ${if (socialCredit >= 0) socialCredit else 0}"

            if (reasons.isNotEmpty()) {
                reasonString += "\nReasons:\n> ${reasons.joinToString("\n> ")}"
                if (reply) {
                    event.reply(reasonString).queue()
                } else {
                    reasonString = event.user.asMention + " " + reasonString
                    event.messageChannel.sendMessage(reasonString).queue()
                }
            }
        }

        fun setSocialCredit(event: MessageReceivedEvent, reasons: List<String>, socialCredit: Int, reply: Boolean = true) {
            setSocialCredit(event.author.id, socialCredit)

            var reasonString = "Your new social credit score: ${if (socialCredit >= 0) socialCredit else 0}"

            if (reasons.isNotEmpty()) {
                reasonString += "\nReasons:\n> ${reasons.joinToString("\n> ")}"
                if (reply) {
                    event.message.reply(reasonString).queue()
                } else {
                    reasonString = event.author.asMention + " " + reasonString
                    event.channel.sendMessage(reasonString).queue()
                }
            }
        }

        fun addSocialCredit(userId: String, socialCredit: Int) {
            if (socialCredit == 0) {
                return
            }
            val currentSocialCredit = getSocialCredit(userId)
            setSocialCredit(userId, currentSocialCredit + socialCredit)
        }

        fun getSocialCreditTier(socialCredit: Int): SocialCreditTier {
            return when (socialCredit) {
                in 0..599 -> SocialCreditTier.D
                in 600..849 -> SocialCreditTier.C
                in 850..959 -> SocialCreditTier.B
                in 960..983 -> SocialCreditTier.AMINUS
                in 984..1006 -> SocialCreditTier.A
                in 1007..1029 -> SocialCreditTier.APLUS
                in 1030..1049 -> SocialCreditTier.AA
                else -> SocialCreditTier.AAA
            }
        }

        fun createUserBanner(member: Member): ByteArray {
            val score = getSocialCredit(member.user.id)

            LOG.debug("Creating user banner for ${member.user.asTag} who has an effective pfp url of ${member.effectiveAvatarUrl} and a social credit of $score")

            val image = ImageIO.read(SocialCreditManager::class.java.getResourceAsStream("/picedit/china.png"))

            val urlRequest = Request.Builder()
                .url(URL(member.effectiveAvatarUrl))
                .build()

            val response = member.jda.httpClient.newCall(urlRequest).execute()

            val imageBytes = response.body?.byteStream()
            val pfpBuffered = ImageIO.read(imageBytes)
            val pfp = pfpBuffered.getScaledInstance(pfpBuffered.width * 2, pfpBuffered.height * 2, Image.SCALE_SMOOTH)

            val bytes = ByteArrayOutputStream()

            val canvas = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
            val graphics = canvas.createGraphics()

            graphics.drawImage(image, 0, 0, null)
            graphics.drawImage(pfp, image.width / 2 - pfpBuffered.width, image.height - 400, null)

            graphics.font = font
            graphics.color = java.awt.Color.YELLOW

            graphics.drawString("Social Credit:", 700, 150)
            graphics.drawString(score.toString(), 700, 250)

            val tier = getSocialCreditTier(score)
            graphics.drawString("Tier: $tier", 700, 350)

            val username = "${member.nickname ?: member.user.name} Citizen no. ${member.user.discriminator}"

            graphics.font = graphics.font.deriveFont(Font.PLAIN, 72f / (username.length / 32f))

            graphics.drawString(username, image.width / 2 - graphics.fontMetrics.stringWidth(username) / 2, image.height - 50)

            graphics.dispose()

            ImageIO.write(canvas, "png", bytes)
            return bytes.toByteArray()
        }
    }
}