package com.example.mobiletreasurehunt.ui

/**
 * Cameron Canfield
 * Mobile Dev
 * Mobile Treasure Hunt
 */

import RulesViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mobiletreasurehunt.ui.data.RuleItem

@Composable
fun StartHuntScreen(navController: NavHostController, timerViewModel: TimerViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Mobile Treasure Hunt!",
            modifier = Modifier
                .padding(top = 50.dp),
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.height(50.dp))
        RulesScreen()
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                timerViewModel.resetTimer()
                timerViewModel.startTimer()
                navController.navigate("ClueScreen/0")
            },
            modifier = Modifier.padding(25.dp)
        ) {
            Text("Start Hunt")
        }
    }
}

@Composable
fun RulesScreen() {
    val context = LocalContext.current
    val viewModel: RulesViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RulesViewModel(context) as T
        }
    })

    val rules by viewModel.rules.collectAsState()
    TreasureHuntRules(rules = rules)
}


@Composable
fun TreasureHuntRules(
    rules: Map<String, RuleItem>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth() // Make it take full width
            .height(500.dp) // Set a fixed height
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Treasure Hunt Rules", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            rules.forEach { (key, ruleItem) ->
                when (ruleItem) {
                    is RuleItem.RuleText -> {
                        Text(
                            text = "$key. ${ruleItem.text}",
                            fontSize = 32.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    is RuleItem.RuleGroup -> {
                        Text(
                            text = "$key.",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        ruleItem.subRules.forEach { (subKey, subText) ->
                            Text(
                                text = "   • $subText",
                                fontSize = 30.sp,
                                modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}