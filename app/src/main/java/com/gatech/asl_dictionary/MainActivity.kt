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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
            Text(
                text = "Current Screen: $currentScreen",
                modifier = Modifier.align(Alignment.Center)
            )
            NavHost(navController = navController, startDestination = "search") {
                currentScreen = "Search"
                composable("search") {
                    Search { navController.navigate("sign") }
                }
                composable("sign") {
                    currentScreen = "Sign"
                    Sign { navController.navigate("search") }
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

