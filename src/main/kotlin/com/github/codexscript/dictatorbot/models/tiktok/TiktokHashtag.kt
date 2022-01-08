package com.github.codexscript.dictatorbot.models.tiktok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
class TiktokHashtag(
    val end: Int = 0,
    val hashtagId: String = "",
    val hashtagName: String? = null,
    val start: Int = 0,
    val type: Int = 0,
    val isCommerce: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (other is TiktokHashtag && this.hashtagName != null) {
            val that = other as TiktokHashtag?
            return this.hashtagName.lowercase() == that!!.hashtagName?.lowercase()
        }
        return super.equals(other)
    }
}
