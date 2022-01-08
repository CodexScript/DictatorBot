package com.github.codexscript.dictatorbot.models.tiktok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Represents a piece of TikTok media that has no other specific class.
 * @property width Width of the media. This may be a non-null value even if the media is audio.
 * @property height Height of the media. This may be a non-null value even if the media is audio.
 * @property uri URI of the media. For playback, url_list should be used.
 * @property urlList List of URIs of the media. The last element of the list is the most reliable.
 */

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class GenericTiktokMedia(
    val width: Int? = null,
    val height: Int? = null,
    val uri: String,
    val urlList: List<String>
)
