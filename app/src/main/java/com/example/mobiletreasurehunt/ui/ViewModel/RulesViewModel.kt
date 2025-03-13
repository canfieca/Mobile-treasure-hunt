import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.mobiletreasurehunt.R
import com.example.mobiletreasurehunt.ui.data.RuleItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class RulesViewModel(context: Context) : ViewModel() {
    private val _rules = MutableStateFlow<Map<String, RuleItem>>(emptyMap())
    val rules: StateFlow<Map<String, RuleItem>> = _rules

    init {
        loadRules(context)
    }

    private fun loadRules(context: Context) {
        val jsonString = context.resources.openRawResource(R.raw.treasure_hunt)
            .bufferedReader().use { it.readText() }
        val jsonObject = Json.parseToJsonElement(jsonString).jsonObject
        val rulesJson = jsonObject["rules"]?.jsonObject ?: return

        val parsedRules = rulesJson.mapValues { (_, value) ->
            if (value is JsonObject) {
                RuleItem.RuleGroup(value.mapValues { it.value.jsonPrimitive.content })
            } else {
                RuleItem.RuleText(value.jsonPrimitive.content)
            }
        }

        _rules.value = parsedRules
    }
}
