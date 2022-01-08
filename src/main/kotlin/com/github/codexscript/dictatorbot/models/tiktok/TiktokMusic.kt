package com.github.codexscript.dictatorbot.models.tiktok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokMusic(
    val avatarMedium: GenericTiktokMedia? = null,
    val collectStat: Int,
    val playUrl: GenericTiktokMedia,
    val author: String,
    val authorDeleted: Boolean,
    val isOriginalSound: Boolean,
    @JsonProperty("sec_uid") val secUID: String? = null,
    val status: Int,
    val id: String,
    val mid: String,
    val isCommerceMusic: Boolean,
    val isMatchedMetadata: Boolean,
    val coverMedium: GenericTiktokMedia? = null,
    val coverThumb: GenericTiktokMedia? = null,
    val coverLarge: GenericTiktokMedia? = null,
    val dmvAutoShow: Boolean,
    val duration: Int,
    val title: String,
    val album: String,
    val muteShare: Boolean,
    val ownerNickname: String,
    val preventDownload: Boolean,
    val userCount: Int,
    val avatarThumb: GenericTiktokMedia? = null,
    val idStr: String,
    @JsonProperty("strong_beat_url") val strongBeatURL: GenericTiktokMedia? = null,
    val videoDuration: Int,
    val offlineDesc: String,
    val ownerHandle: String,
    val previewStartTime: Double,
    val isAuthorArtist: Boolean,
    val isOriginal: Boolean,
    val isPgc: Boolean,
    val shootDuration: Int,
    val auditionDuration: Int,
)
