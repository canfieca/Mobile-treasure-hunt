package com.example.mobiletreasurehunt.ui

/**
 * Cameron Canfield
 * Mobile Dev
 * Mobile Treasure Hunt
 */


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun HuntDoneScreen(
    navController: NavController,
    clueInfo: String,
    elapsedTime: Long,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    val decodedClueInfo = URLDecoder.decode(clueInfo, StandardCharsets.UTF_8.toString())

    Box(
        modifier = modifier
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Congratulations you completed the Mobile Treasure Hunt!", fontSize = 24.sp)
            Text("Last Clue Location: $decodedClueInfo", fontSize = 18.sp)
            Text("Total Time: ${elapsedTime}s", fontSize = 18.sp)
            Button(
                onClick = { navController.navigate("startHuntScreen") },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Home")
            }
        }
    }
}