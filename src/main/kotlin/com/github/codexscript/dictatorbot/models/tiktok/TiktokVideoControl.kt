package com.github.codexscript.dictatorbot.models.tiktok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokVideoControl(
    val allowDownload: Boolean,
    val allowDuet: Boolean,
    val allowDynamicWallpaper: Boolean,
    val allowReact: Boolean,
    val preventDownloadType: Int,
    val shareType: Int,
    val allowMusic: Boolean,
    val allowStitch: Boolean,
    val draftProgressBar: Int,
    val showProgressBar: Int,
    val timerStatus: Int,
)
