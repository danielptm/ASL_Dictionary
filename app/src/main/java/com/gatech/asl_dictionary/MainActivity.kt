package com.gatech.asl_dictionary

import com.gatech.asl_dictionary.ui.theme.ASL_DictionaryTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import com.ccg.slrcore.engine.SimpleExecutionEngine
import com.ccg.slrcore.common.*
import kotlinx.coroutines.flow.MutableStateFlow
import android.widget.Toast
import com.ccg.slrcore.system.*

lateinit var SLREngine: SimpleExecutionEngine
public val currResult = MutableStateFlow(
    ImageMPResultWrapper(Empties.EMPTY_HANDMARKER_RESULTS, Empties.EMPTY_BITMAP)
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        SLREngine = SimpleExecutionEngine(this, {
            SLREngine.signPredictor.outputFilters.add(Thresholder(0.8F))
        }) { sign ->
            runOnUiThread {
                Toast.makeText(this, "Guessed: $sign", Toast.LENGTH_SHORT).show()
            }
        }
        SLREngine.buffer.trigger = NoTrigger()
        SLREngine.posePredictor.addCallback("preview_update") { mpResult ->
            currResult.value = mpResult
        }
        super.onCreate(savedInstanceState)
        setContent {
            ASL_DictionaryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Start()
                }
            }
        }
    }
}

@Composable
fun Start() {
    val navController = rememberNavController()
    var currentScreen by remember { mutableStateOf("Search") }
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavHost(navController = navController, startDestination = "search") {
                currentScreen = "Search"
                composable("search") {
                    Search(navController)
                }
                composable("sign") {
                    currentScreen = "Sign"
                    Sign()
                }
                composable("result/{param}") {backStackEntry ->
                    currentScreen = "Result"
                    val param = backStackEntry.arguments?.getString("param")
                    Result(param = param)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = Color.Gray)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                navController.navigate("search") {
                    popUpTo("search") { inclusive = true }
                    launchSingleTop = true
                }
            }) {
                Text("Search")
            }
            Button(onClick = {
                navController.navigate("sign") {
                    popUpTo("sign") { inclusive = true }
                    launchSingleTop = true
                }
            }) {
                Text("Sign")
            }
        }
    }
}

