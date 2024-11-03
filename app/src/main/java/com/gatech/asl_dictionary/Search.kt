package com.gatech.asl_dictionary

import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
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
import coil.compose.rememberAsyncImagePainter
import com.gatech.asl_dictionary.model.SignEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
                System.out.println("hi")
            }) {
                Text("Search")
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