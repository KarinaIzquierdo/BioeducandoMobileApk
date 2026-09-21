package com.bioeducando.mobile.viewmodels.admin

import com.bioeducando.mobile.data.model.admin.RecentActivity

data class AdminDashboardUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val usersCount: Int = 0,
    val missionsCount: Int = 0,
    val interactionsCount: Int = 0,
    val activities: List<RecentActivity> = emptyList()
)
