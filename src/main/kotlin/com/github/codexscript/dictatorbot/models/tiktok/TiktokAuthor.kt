package com.github.codexscript.dictatorbot.models.tiktok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokAuthor(
    /*
    TikTok API programmers are very smart, and they know what they are doing.
    They decided to set all the author's fields to null if the video is removed instead of just setting the author field to null.
    So, we have to do this.
     */

    val enterpriseVerifyReason: String? = null,
    val isAdFake: Boolean? = null,
    val isBlock: Boolean? = null,
    val status: Int? = null,
    val verifyInfo: String? = null,
    val UID: String? = null,
    @JsonProperty("user_canceled") val userCancelled: Boolean? = null,
    val userMode: Int? = null,
    @JsonProperty("ad_cover_url") val adCoverURL: String? = null,
    val hasFacebookToken: Boolean? = null,
    val stitchSetting: Int? = null,
    val userRate: Int? = null,
    val avatarThumb: GenericTiktokMedia? = null,
    val isStar: Boolean? = null,
    val nickname: String? = null,
    @JsonProperty("sec_uid") val secUID: String? = null,
    val acceptPrivacyPolicy: Boolean? = null,
    val avatarMedium: GenericTiktokMedia? = null,
    val twitterName: String? = null,
    @JsonProperty("unique_id") val uniqueID: String? = null,
    val duetSetting: Int? = null,
    val avatarLarger: GenericTiktokMedia? = null,
    val withCommerceEntry: Boolean? = null,
    @JsonProperty("ins_id") val instagramID: String? = null,
    val signature: String? = null,
    @JsonProperty("avatar_uri") val avatarURI: String? = null,
    @JsonProperty("twitter_id") val twitterID: String? = null,
)
