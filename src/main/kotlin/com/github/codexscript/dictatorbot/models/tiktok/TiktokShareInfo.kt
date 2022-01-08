package com.github.codexscript.dictatorbot.models.tiktok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokShareInfo(
    @JsonProperty("share_url") val shareURL: String,
    val shareDesc: String? = null,
    val shareLinkDesc: String? = null,
    val shareQuote: String? = null,
    val shareSignatureDesc: String? = null,
    @JsonProperty("share_signature_url") val shareSignatureURL: String? = null,
    val shareTitle: String? = null,
    val boolPersist: Int? = null,
    val shareTitleMyself: String? = null,
    val shareTitleOther: String? = null,
    val shareWeiboDesc: String? = null,
    val shareDescInfo: String? = null
)
