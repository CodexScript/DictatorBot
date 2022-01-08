package com.github.codexscript.dictatorbot.models.tiktok

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@JsonIgnoreProperties(ignoreUnknown = true)
data class TiktokVideoListing(
    @JsonProperty("author_user_id") val authorUserID: String? = null,
    //val geofencing: Any? = null,
    //val long_video: Any? = null,
    val preventDownload: Boolean? = null,
    //val search_highlight: Any? = null,
    val statistics: TiktokVideoStatistics? = null,
    val followUpPublishFromId: String? = null,
    val haveDashboard: Boolean? = null,
    val playlistBlocked: Boolean? = null,
    //val mask_infos: List<Any>? = null,
    //val image_infos: List<Any>? = null,
    val miscInfo: String? = null,
    val isTop: Int? = null,
    val labelTopText: String? = null,
    val withPromotionalMusic: Boolean? = null,
    //val products_info: List<Any>? = null,
    //val video_labels: List<Any>? = null,
    @JsonProperty("aweme_id") val awemeID: String,
    val collectStat: Int? = null,
    @JsonProperty("group_id") val groupID: String? = null,
    val region: String? = null,
    val userDigged: Int? = null,
    val shareInfo: TiktokShareInfo,
    val music: TiktokMusic? = null,
    val contentDesc: String? = null,
    val desc: String? = null,
    val labelTop: GenericTiktokMedia? = null,
    val awemeType: Int,
    val itemDuet: Int? = null,
    val isPgcshow: Boolean? = null,
    val videoControl: TiktokVideoControl? = null,
    val bodydanceScore: Int? = null,
    val video: TiktokVideo? = null,
    val cmtSwt: Boolean? = null,
    val isVr: Boolean = false,
    val rate: Int? = null,
    val textExtra: List<TiktokHashtag>? = null,
    val distance: String? = null,
    val createTime: Int = 0,
    val canPlay: Boolean = true,
)