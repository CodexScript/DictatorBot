package com.github.codexscript.dictatorbot.models.tiktok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokFavsResponse(
    val hasMore: Int,
    val cursor: Int,
    val awemeList: List<TiktokVideoListing>,
    val statusCode: Int,
)
