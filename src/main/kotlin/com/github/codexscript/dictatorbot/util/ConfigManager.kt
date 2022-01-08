package com.github.codexscript.dictatorbot.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.*
import com.github.codexscript.dictatorbot.models.config.Config
import org.slf4j.LoggerFactory
import java.io.File

class ConfigManager {
    companion object {
        private val LOG = LoggerFactory.getLogger(ConfigManager::class.java)
        private val configFolder = System.getProperty("user.dir") + "/data/"
        private val config = File(System.getProperty("user.dir") + "/data/config.yml")
        private var configData: Config? = null
        private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

        private fun createDefaultConfig(): Config {
            LOG.debug("Creating default config at ${config.path}")
            val default = ConfigManager::class.java.getResource("/config_default.yml").readText()
            if (File(configFolder).mkdirs()) {
                LOG.debug("Created config folder at $configFolder")
            }
            File(config.path).writeText(default)
            configData = mapper.readValue(default)
            return configData!!
        }

        fun getConfigContent(): Config {
            if (!config.exists()) {
                return createDefaultConfig()
            }
            if (configData == null) {
                configData = mapper.readValue(config.readText())
            }
            return configData!!
        }
    }
}