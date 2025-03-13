import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun ClueSolvedPage(navController: NavHostController, clueInfo: String, clueIndex: Int) {

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
            Text(clueInfo, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(30.dp))

            // Button to go to the next clue
            Button(onClick = {
                navController.navigate("ClueScreen/${clueIndex}") {
                    popUpTo("ClueScreen/{clueIndex}") { inclusive = true }
                }

            }) {
                Text("Next Clue")
            }
        }
    }
}
