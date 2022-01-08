package com.github.codexscript.dictatorbot.models.tiktok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokVideoStatistics(
    val loseCount: Int? = null,
    val playCount: Int,
    val whatsappShareCount: Int? = null,
    @JsonProperty("aweme_id") val awemeID: String,
    val commentCount: Int,
    val diggCount: Int,
    val downloadCount: Int,
    val forwardCount: Int,
    val loseCommentCount: Int? = null,
    val shareCount: Int
)
