package com.github.codexscript.dictatorbot.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import java.io.File

class ConfigManager {
    companion object {
        private val LOG = LoggerFactory.getLogger(ConfigManager::class.java)
        private val configFolder = System.getProperty("user.dir") + "/data/"
        private val config = File(System.getProperty("user.dir") + "/data/config.json")

        private fun createDefaultConfig(): Map<*, *> {
            LOG.debug("Creating default config at ${config.path}")
            val default = ConfigManager::class.java.getResource("/config_default.json").readText()
            if (File(configFolder).mkdirs()) {
                LOG.debug("Created config folder at $configFolder")
            }
            File(config.path).writeText(default)
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