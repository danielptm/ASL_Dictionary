package com.gatech.asl_dictionary

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import android.widget.Toast
import android.content.Context
import android.view.Gravity

suspend fun loadSignEntries(context: Context): List<SignData> {
    val sdi: SignDataInterface = SignDataLocalStore();
    val res = sdi.getData();

    return res
}

fun searchByText(text: String): SignData {
    val sdi: SignDataInterface = SignDataLocalStore();
    val res = sdi.getSignDataByText(text);
    return res;
}

@Composable
fun Search(navController: NavController) {
    val context = LocalContext.current
    var signEntries by remember { mutableStateOf(emptyList<SignData>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        signEntries = loadSignEntries(context)
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            Text("Loading...")
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(signEntries) { entry ->
                    SignBox(entry) {
                        val param = entry.word + "|" + entry.imagePath + "|" + entry.videoPath
                        navController.navigate("result/$param")
                    }
                }
            }
        }

        var inputText by remember { mutableStateOf("") }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Search") }
            )
            Button(onClick = {
                val res = searchByText(inputText)
                if (res.word.isEmpty()) {
                    Toast.makeText(context, "Not found", Toast.LENGTH_LONG).apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
                } else {
                    val param = "${res.word}|${res.imagePath}|${res.videoPath}"
                    navController.navigate("result/$param")
                }
            }) {
                Text("Search")
            }
        }
    }
}

@Composable
fun SignBox(signEntry: SignData, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter("file:///android_asset/images/${signEntry.imagePath}"),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = signEntry.word)
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    val navController = rememberNavController()
    Search(navController)
}