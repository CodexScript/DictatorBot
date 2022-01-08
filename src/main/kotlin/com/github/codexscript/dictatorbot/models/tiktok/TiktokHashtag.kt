package com.github.codexscript.dictatorbot.models.tiktok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokHashtag(
    val end: Int = 0,
    val hashtagId: String = "",
    val hashtagName: String? = null,
    val start: Int = 0,
    val type: Int = 0,
    val isCommerce: Boolean = false
)
