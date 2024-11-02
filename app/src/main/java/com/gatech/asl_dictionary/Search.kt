package com.gatech.asl_dictionary

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gatech.asl_dictionary.model.SignEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.Image
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

suspend fun loadSignEntries(context: Context): List<SignEntry> {
    return withContext(Dispatchers.IO) {
        val jsonString = context.assets.open("Signs.json").bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<SignEntry>>() {}.type
        Gson().fromJson(jsonString, listType)
    }
}

@Composable
fun Search(navigationToSecondScreen: (String) -> Unit) {
    val context = LocalContext.current
    var signEntries by remember { mutableStateOf(emptyList<SignEntry>()) }
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
                        navigationToSecondScreen(entry.word)
                    }
                }
            }
        }
    }
}

@Composable
fun SignBox(signEntry: SignEntry, onClick: () -> Unit) {
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
    Search({})
}