package com.example.mobiletreasurehunt

import ClueSolvedPage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.mobiletreasurehunt.ui.ClueScreen
import com.example.mobiletreasurehunt.ui.HuntDoneScreen
import com.example.mobiletreasurehunt.ui.PermissionsScreen
import com.example.mobiletreasurehunt.ui.StartHuntScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        LocationManager.initialize(this)
        setContent {
            TreasureHuntApp()
        }
    }
}

@Composable
fun TreasureHuntApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "permissionsScreen") {
        composable("permissionsScreen") { PermissionsScreen(navController) }
        composable("startHuntScreen") { StartHuntScreen(navController) }

        // ClueScreen expects an index to track the current clue
        composable("ClueScreen/{clueIndex}") { backStackEntry ->
            val clueIndex = backStackEntry.arguments?.getString("clueIndex")?.toIntOrNull() ?: 0
            ClueScreen(navController, clueIndex)
        }

        // ClueSolvedScreen expects the clue info and index for tracking progress
        composable("ClueSolvedScreen/{clueInfo}/{clueIndex}") { backStackEntry ->
            val clueInfo = backStackEntry.arguments?.getString("clueInfo") ?: "No information available"
            val clueIndex = backStackEntry.arguments?.getString("clueIndex")?.toIntOrNull() ?: 0
            ClueSolvedPage(navController, clueInfo, clueIndex)
        }

        // HuntDoneScreen expects the clue info for the last clue
        composable("HuntDoneScreen/{clueInfo}") { backStackEntry ->
            val clueInfo = backStackEntry.arguments?.getString("clueInfo") ?: "No information available"
            HuntDoneScreen(clueInfo)
        }
    }
}