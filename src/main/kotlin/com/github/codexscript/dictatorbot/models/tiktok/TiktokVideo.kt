package com.github.codexscript.dictatorbot.models.tiktok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokVideo(
    val aiDynamicCoverBak: GenericTiktokMedia? = null,
    val dynamicCover: GenericTiktokMedia? = null,
    val hasDownloadSuffixLogoAddr: Boolean? = null,
    val height: Int? = null,
    val width: Int? = null,
    val miscDownloadAddrs: String? = null,
    val needSetToken: Boolean? = null,
    val aiDynamicCover: GenericTiktokMedia? = null,
    val duration: Int? = null,
    val hasWatermark: Boolean? = null,
    val isCallback: Boolean? = null,
    val originCover: GenericTiktokMedia? = null,
    val cdnUrlExpired: Int? = null,
    val bitRate: List<TiktokVideoQuality>? = null,
    val ratio: String? = null,
    val downloadAddr: TiktokDownloadAddr? = null,
    val playAddr: TiktokPlayAddr? = null,
    val isH265: Int? = null,
    val downloadSuffixLogoAddr: GenericTiktokMedia? = null,
    val cover: GenericTiktokMedia? = null,
    val isBytevc1: Int? = null,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokVideoQuality(
    val isBytevc1: Int,
    val isH265: Int,
    val playAddr: TiktokPlayAddr,
    val qualityType: Int,
    val bitRate: Int,
    val gearName: String,
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokPlayAddr(
    val height: Int,
    val uri: String,
    val urlKey: String,
    val urlList: List<String>,
    val width: Int,
    val dataSize: Int,
    val fileCs: String? = null,
    val fileHash: String
)

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokDownloadAddr(
    val dataSize: Int,
    val height: Int,
    val uri: String,
    val urlList: List<String>,
    val width: Int
)
