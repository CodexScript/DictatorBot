package com.github.codexscript.dictatorbot.models.config


data class Config(
    val ownerID: String,
    val socialCreditPhrases: Map<String, Int> = mapOf(),
    val socialCreditRegex: Map<String, Int> = mapOf(),
    val invalidCommandPenalty: Int = 25,
    val lavalinkIP: String,
    val lavalinkPort: Int,
    val lavalinkPassword: String,
    val minecraftServerIP: String? = null,
    val rconPassword: String? = null,
    val rconPort: Int = 25575,
    val symbolabUsername: String? = null,
    val symbolabPassword: String? = null,
    val geckodriverPath: String? = null,
    val tiktok: TiktokConfig? = null
)