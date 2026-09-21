package com.bioeducando.mobile.data.model.admin

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    @SerializedName("users_count") val users_count: Int,
    @SerializedName("missions_count") val missions_count: Int,
    @SerializedName("interactions_count") val interactions_count: Int,
    @SerializedName("recent_activities") val recent_activities: List<RecentActivity>
)

data class RecentActivity(
    @SerializedName("type") val type: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("time_ago") val timeAgo: String
)
