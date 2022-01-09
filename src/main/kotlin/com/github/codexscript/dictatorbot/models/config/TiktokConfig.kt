package com.github.codexscript.dictatorbot.models.config

data class TiktokConfig(
    val xKhronos: String,
    val xGorgon: String,
    val cookie: String,
    val badSounds: List<String>? = null,
    val badHashtags: List<String>? = null,
    val badAuthors: List<String>? = null
)
