package com.example.mobiletreasurehunt.ui.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import android.content.Context

@Serializable
data class Rules(
    val rules: Map<String, RuleItem>
)

@Serializable
sealed class RuleItem {
    @Serializable
    data class RuleText(val text: String) : RuleItem()

    @Serializable
    data class RuleGroup(val subRules: Map<String, String>) : RuleItem()
}
