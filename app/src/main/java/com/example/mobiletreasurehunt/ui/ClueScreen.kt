package com.example.mobiletreasurehunt.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.mobiletreasurehunt.R
import com.example.mobiletreasurehunt.ui.data.TreasureHunt
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.*
import java.io.InputStreamReader
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ClueScreen(navController: NavHostController, clueIndex: Int) {
    val context = LocalContext.current
    val treasureHunt = remember { loadTreasureHunt(context) }
    val backStackEntry = navController.currentBackStackEntry
    val passedIndex = backStackEntry?.arguments?.getString("clueIndex")?.toIntOrNull() ?: 0
    var currentClueIndex by remember { mutableStateOf(clueIndex) }
    var showHint by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var markerPosition by remember { mutableStateOf<LatLng?>(null) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val currentClue = treasureHunt.clues[currentClueIndex]
    val clueLocation = LatLng(currentClue.longitude, currentClue.latitude)

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    userLocation = LatLng(it.latitude, it.longitude)
                    markerPosition = null // Reset marker so user can place it manually
                    Log.d("ClueScreen", "User location updated: $userLocation")
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Clue Screen", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Text(currentClue.clue, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(20.dp))
        if (showHint) {
            Text(currentClue.hint, fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(20.dp))
        }
        Button(onClick = { showHint = true }) {
            Text("Show Hint")
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Google Map Display
        Box(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
        ) {
            val mapProperties = remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
            val uiSettings = remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = mapProperties.value,
                uiSettings = uiSettings.value,
                onMapClick = { position ->
                    markerPosition = position
                }
            ) {
                markerPosition?.let {
                    Marker(
                        state = rememberMarkerState(position = it),
                        title = "Selected Location",
                        draggable = true,
                        onClick = { marker ->
                            markerPosition = marker.position
                            false // Returning false allows default behavior
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            markerPosition?.let { location ->
                if (isUserAtLocation(location, clueLocation)) {
                    val encodedClueInfo = URLEncoder.encode(currentClue.info, StandardCharsets.UTF_8.toString())
                    val nextClueIndex = currentClueIndex + 1
                    if (currentClueIndex == 2) { // Assuming 2 is the last clue index
                        navController.navigate("HuntDoneScreen/$encodedClueInfo") {
                            popUpTo("ClueScreen/{clueIndex}") { inclusive = true }
                        }
                    } else {
                        navController.navigate("ClueSolvedScreen/$encodedClueInfo/$nextClueIndex")
                    }
                } else {
                    Log.d("ClueScreen", "Marker is too far from the clue location.")
                }
            } ?: Log.d("ClueScreen", "No marker placed.")
        }) {
            Text("Check Location")
        }



        Spacer(modifier = Modifier.height(20.dp))
    }
}

private const val REQUEST_LOCATION_PERMISSION = 1

fun loadTreasureHunt(context: Context): TreasureHunt {
    val inputStream = context.resources.openRawResource(R.raw.treasure_hunt)
    val reader = InputStreamReader(inputStream)
    return Gson().fromJson(reader, TreasureHunt::class.java)
}

fun isUserAtLocation(userLocation: LatLng, targetLocation: LatLng, radius: Float = 150f): Boolean {
    val results = FloatArray(1)
    Log.d("isUserAtLocation: ", "${userLocation}, ${targetLocation}")
    Location.distanceBetween(
        userLocation.latitude, userLocation.longitude,
        targetLocation.latitude, targetLocation.longitude,
        results
    )
    return results[0] <= radius
}