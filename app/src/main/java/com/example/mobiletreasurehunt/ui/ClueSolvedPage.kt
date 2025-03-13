package com.example.mobiletreasurehunt.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun ClueSolvedPage(navController: NavHostController, clueInfo: String, clueIndex: Int, timerViewModel: TimerViewModel) {
    LaunchedEffect(Unit) {
        Log.d("ClueSolvedPage", "Stopping timer at ${timerViewModel.elapsedTime.value}s")
        timerViewModel.stopTimer()
    }

    val decodedClueInfo = URLDecoder.decode(clueInfo, StandardCharsets.UTF_8.toString())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Clue Solved!", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Text(decodedClueInfo, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(30.dp))

            // Button to go to the next clue
            Button(onClick = {
                timerViewModel.startTimer()
                navController.navigate("ClueScreen/${clueIndex}") {
                    popUpTo("ClueScreen/{clueIndex}") { inclusive = true }
                }
            }) {
                Text("Continue")
            }
        }
    }
}