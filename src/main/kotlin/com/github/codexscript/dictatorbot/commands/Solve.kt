package com.github.codexscript.dictatorbot.commands

import com.github.codexscript.dictatorbot.models.WorkerOwnedSlashCommand
import com.github.codexscript.dictatorbot.util.ConfigManager
import com.google.common.net.UrlEscapers
import com.jagrosh.jdautilities.command.SlashCommand
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import org.openqa.selenium.By
import org.openqa.selenium.Cookie
import org.slf4j.LoggerFactory
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.*
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*
import kotlin.concurrent.thread


class Solve : SlashCommand() {
    private val LOG = LoggerFactory.getLogger(Solve::class.java)

    init {
        name = "solve"
        help = "Grab solution for a given problem from academic resources such as Symbolab"
        children = arrayOf(
            Symbolab()
        )

        if (config.geckodriverPath != null) {
            System.setProperty("webdriver.gecko.driver", config.geckodriverPath)
        }
    }

    override fun execute(event: SlashCommandEvent?) {
    }

    companion object {
        val config = ConfigManager.getConfigContent()
        private class Symbolab : WorkerOwnedSlashCommand() {
            companion object {

                val username = config.symbolabUsername
                val password = config.symbolabPassword
            }
            private val LOG = LoggerFactory.getLogger("Solve:Symbolab")
            init {
                name = "symbolab"
                help = "Solver for Symbolab"
                options = listOf(
                    OptionData(
                        OptionType.STRING,
                        "problem",
                        "LaTeX problem to solve (copy-paste from Symbolab)",
                        true
                    )
                )
                reward = 10
            }

            fun getDriver(): FirefoxDriver {
                val caps = FirefoxOptions()
                caps.setCapability("os", "Windows")
                caps.setCapability("os_version", "10")
                caps.setCapability("browser", "Chrome")
                caps.setCapability("browser_version", "latest")
                caps.setCapability("browserstack.local", "false")
                caps.setCapability("browserstack.video", "false")
                caps.setCapability("browserstack.selenium_version", "3.14.0")
                caps.setCapability("browserstack.seleniumLogs", "false")
                caps.addArguments(mutableListOf("--headless", "--disable-gpu", "--window-size=1920,1080", "--start-maximized", "--start-fullscreen"))

                return FirefoxDriver(caps)
            }

            fun login(driver: FirefoxDriver): Boolean {
                LOG.debug("Logging in")
                val signIn = driver.findElement(By.id("signIn"))
                signIn.click()
                Thread.sleep(100)
                WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("g_id_onload")))
                LOG.debug("Found sign in form")

                driver.executeScript("document.getElementById('signin_email').value = '$username'")
                driver.executeScript("document.getElementById('signin_password').value = '$password'")
                driver.executeScript("document.getElementById('rememberMe').checked = true")

                driver.findElement(By.className("featherlight-close")).click()
                Thread.sleep(100)
                signIn.click()
                Thread.sleep(100)

                driver.executeScript("document.getElementsByClassName('nl-signInButton')[0].click()")

                Thread.sleep(100)
                driver.get(driver.currentUrl)

                val cookies = driver.manage().cookies
                val token = cookies.find { it.name == "sy2.token" }
                if (token == null) {
                    LOG.error("Failed to login")
                    return false
                } else {
                  val cookiesFile = File("./data/cookies.data")
                  if (cookiesFile.exists()) {
                    cookiesFile.delete()
                  }
                  cookiesFile.createNewFile()
                    val writer = FileWriter(cookiesFile)
                    val Bwrite = BufferedWriter(writer)
                    for (ck in cookies) {
                        Bwrite.write((ck.name +";"+ck.value +";"+ck.domain +";"+ck.path +";"+ck.expiry +";"+ck.isSecure))
                        Bwrite.newLine()
                    }
                    Bwrite.close()
                    writer.close()
                }
                return true
            }

            fun setCookiesFromDisk(driver: FirefoxDriver): Boolean {
                val cookiesFile = File("./data/cookies.data")
                if (cookiesFile.exists()) {
                    val reader = FileReader(cookiesFile)
                    val breader = BufferedReader(reader)

                    var strline = breader.readLine()
                    while (strline != null) {
                        val token = StringTokenizer(strline, ";")
                        while(token.hasMoreTokens()) {
                            val name = token.nextToken()
                            val value = token.nextToken()
                            val domain = token.nextToken()
                            val path = token.nextToken()
                            var expiry = Date()

                            val dateToken = token.nextToken()
                            if (dateToken != null) {
                                try {
                                    val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
                                    expiry = formatter.parse(dateToken)
                                } catch (e: ParseException) {
                                }
                            }

                            val isSecure = token.nextToken().toBoolean()
                            val ck = Cookie(name, value, domain, path, expiry, isSecure)
                            LOG.debug("Adding cookie: $ck")
                            driver.manage().addCookie(ck)
                        }
                        strline = breader.readLine()
                    }
                    return true
                }
                else {
                    return false
                }
            }

            override fun execute(event: SlashCommandEvent?) {
                if (event == null) {
                    return
                }

                if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                    event.reply("Username/password not set").setEphemeral(true).queue()
                    return
                }

                if (config.geckodriverPath == null || config.geckodriverPath.isEmpty()) {
                    event.reply("Geckodriver path not set").setEphemeral(true).queue()
                    return
                }

                val problem = event.getOption("problem")!!.asString
                LOG.debug("Solving problem: $problem")

                event.deferReply().queue()

                thread(start = true) {
                    val driver = getDriver()

                    try {

                        driver.get("https://www.symbolab.com/solver/step-by-step/${UrlEscapers.urlFragmentEscaper().escape(problem)}?or=input")
                        if (driver.title == "Not found") {
                            event.hook.editOriginal("Problem not found").queue()
                            return@thread
                        }
                        LOG.debug("Current URL: {}", driver.currentUrl)
                        val problemURL = driver.currentUrl

                        var cookies = driver.manage().cookies

                        var token = cookies.find { it.name == "sy2.token" }

                        var loginSuccess = false

                        if (token == null) {
                            LOG.debug("No token found, logging in")
                            loginSuccess = setCookiesFromDisk(driver)
                            if (!loginSuccess) {
                                loginSuccess = login(driver)
                                if (!loginSuccess) {
                                    LOG.warn("Failed to log in, using guest Symbolab session")
                                }
                            }
                            driver.get(problemURL)
                        }

                        val element = WebDriverWait(driver, Duration.ofSeconds(30)).until(
                            ExpectedConditions.presenceOfElementLocated(By.className("solution_div"))
                        )

                        if (element == null) {
                            LOG.warn("Timeout while waiting for solution")
                            event.hook.editOriginal("Timeout while waiting for solution").queue()
                            driver.quit()
                            return@thread
                        }

                        Thread.sleep(2000)

                        driver.executeScript("document.getElementsByClassName('nl-cookiepolicy')[0].remove()")

                        if (loginSuccess) {
                            var showSteps = driver.findElements(By.className("showStepsButton"))
                            while (showSteps.isNotEmpty()) {
                                for (button in showSteps) {
                                    button.click()

                                }
                                showSteps = driver.findElements(By.className("showStepsButton"))
                            }
                        }

                        val screen = element.getScreenshotAs(org.openqa.selenium.OutputType.BYTES)
                        if (screen == null) {
                            LOG.warn("Screenshot is null")
                            event.hook.editOriginal("Screenshot could not be captured").queue()
                            driver.quit()
                            return@thread
                        }

                        event.hook.editOriginal(screen, "solution.png").queue()
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                        event.hook.editOriginal("Error while solving problem: ```\n${e.message}\n```").queue()
                    }
                    finally {
                        driver.quit()
                    }
                }
                rewardScore(event, false)
            }
        }

        private class Chegg : SlashCommand() {
            private val LOG = LoggerFactory.getLogger(Chegg::class.java)

            init {
                name = "chegg"
                help = "Solver for Chegg"
                options = listOf(
                    OptionData(
                        OptionType.STRING,
                        "problem",
                        "URL of the problem to solve",
                        true
                    )
                )
            }

            override fun execute(event: SlashCommandEvent?) {

            }


        }
    }
}