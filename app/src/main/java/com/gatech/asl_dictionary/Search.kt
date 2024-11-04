package com.gatech.asl_dictionary

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter

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
            LazyColumn {
                items(signEntries) { entry ->
                    SignBox(entry) {
                        val param = entry.word + "|" + entry.imagePath + "|" + entry.videoPath
                        navController.navigate("result/" + param)
                    }
                }
            }
        }


        var inputText by remember { mutableStateOf("") }

        Spacer(modifier = Modifier.height(420.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ){
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Search") }
            )
            Button(onClick = {
                val res = searchByText(inputText)
                val param = res.word + "|" + res.imagePath + "|" + res.videoPath
                navController.navigate("result/" + param)
            }) {
                Text("Search")
            }

        }
    }
}

@Composable
fun SignBox(signEntry: SignData, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter("file:///android_asset/images/${signEntry.imagePath}"),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Text(text = signEntry.word)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    val navController = rememberNavController()
    Search(navController)
}