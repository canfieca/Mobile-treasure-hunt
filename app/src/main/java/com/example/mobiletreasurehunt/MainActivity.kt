package com.example.mobiletreasurehunt

/**
 * Cameron Canfield
 * Mobile Dev
 * Mobile Treasure Hunt
 */

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.mobiletreasurehunt.ui.ClueScreen
import com.example.mobiletreasurehunt.ui.HuntDoneScreen
import com.example.mobiletreasurehunt.ui.PermissionsScreen
import com.example.mobiletreasurehunt.ui.StartHuntScreen
import com.example.mobiletreasurehunt.ui.TimerViewModel
import com.example.mobiletreasurehunt.ui.ClueSolvedPage
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.pm.PackageManager

class MainActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var permissionGranted by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        LocationManager.initialize(this)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            permissionGranted = isGranted
        }

        // Check initial permission status
        val permissionCheck = ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        )
        permissionGranted = permissionCheck == PackageManager.PERMISSION_GRANTED

        setContent {
            TreasureHuntApp(permissionGranted, requestPermissionLauncher)
        }
    }
}

@Composable
fun TreasureHuntApp(permissionGranted: Boolean, requestPermissionLauncher: ActivityResultLauncher<String>) {
    val navController = rememberNavController()
    val timerViewModel: TimerViewModel = viewModel()

    // Collect the elapsed time from the StateFlow
    val elapsedTime by timerViewModel.elapsedTime.collectAsState()

    NavHost(navController = navController, startDestination = "permissionsScreen") {
        composable("permissionsScreen") {
            PermissionsScreen(navController, permissionGranted) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
        composable("startHuntScreen") { StartHuntScreen(navController, timerViewModel) }

        composable("ClueScreen/{clueIndex}") { backStackEntry ->
            val clueIndex = backStackEntry.arguments?.getString("clueIndex")?.toIntOrNull() ?: 0
            ClueScreen(navController, clueIndex, timerViewModel)
        }

        composable("ClueSolvedScreen/{clueInfo}/{clueIndex}") { backStackEntry ->
            val clueInfo = backStackEntry.arguments?.getString("clueInfo") ?: "No information available"
            val clueIndex = backStackEntry.arguments?.getString("clueIndex")?.toIntOrNull() ?: 0
            ClueSolvedPage(navController, clueInfo, clueIndex, timerViewModel)
        }

        composable("HuntDoneScreen/{clueInfo}") { backStackEntry ->
            val clueInfo = backStackEntry.arguments?.getString("clueInfo") ?: "No information available"

            // Pass the collected elapsedTime and navController
            HuntDoneScreen(navController, clueInfo, elapsedTime)
        }
    }
}