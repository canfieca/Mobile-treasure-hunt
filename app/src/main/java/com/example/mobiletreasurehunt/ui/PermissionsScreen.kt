package com.example.mobiletreasurehunt.ui

/**
 * Cameron Canfield
 * Mobile Dev
 * Mobile Treasure Hunt
 */

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.LocationServices
import androidx.activity.result.contract.ActivityResultContracts

@Composable
fun PermissionsScreen(navController: NavHostController, permissionGranted: Boolean, requestPermission: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Location Permission Required", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(20.dp))

        if (permissionGranted) {
            Button(
                onClick = { navController.navigate("startHuntScreen") }
            ) {
                Text("Proceed to Start Hunt")
            }
        } else {
            Button(
                onClick = { requestPermission() }
            ) {
                Text("Grant Location Permission")
            }
        }
    }
}