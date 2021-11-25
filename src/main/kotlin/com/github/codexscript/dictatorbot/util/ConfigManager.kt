package com.github.codexscript.dictatorbot.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.io.File

class ConfigManager {
    companion object {
        private val LOG = LoggerFactory.getLogger(ConfigManager::class.java)
        private val configPath = System.getProperty("user.dir") + "/data/config.json"
        private val config = File(System.getProperty("user.dir") + "/data/config.json")

        private fun createDefaultConfig(): Map<*, *> {
            LOG.debug("Creating default config at $configPath")
            val default = ConfigManager::class.java.getResource("/config_default.json").readText()
            File(configPath).writeText(default)
            val mapper = ObjectMapper()
            return mapper.readValue(default, Map::class.java)
        }

        fun getConfigContent(): Map<*, *> {
            if (!config.exists()) {
                return createDefaultConfig()
            }
            val mapper = ObjectMapper()
            return mapper.readValue(config.readText(), Map::class.java)
        }
    }
}