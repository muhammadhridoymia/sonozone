package com.example.sonozone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage

data class DemoStorys(
    val id: String,
    val title: String,
    val author: String,
    val imageUrl: String
)

@Composable
fun LibraryScreen(modifier: Modifier = Modifier) {

    val savedStories = listOf(
        DemoStorys("1", "The Lost Kingdom", "Sarah Johnson", "https://picsum.photos/200/200"),
        DemoStorys("2", "Midnight Dreams", "Michael Chen", "https://picsum.photos/200/201"),
        DemoStorys("3", "Ocean Secret", "Emma Wilson", "https://picsum.photos/200/202"),
        DemoStorys("4", "Forest Tales", "Lisa Anderson", "https://picsum.photos/200/203")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        // Header
        Text(
            text = "Your Library",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Story List
        LazyColumn(
            contentPadding = PaddingValues(8.dp)
        ) {
            items(savedStories) { story ->
                LibraryItem( story)
            }
        }
    }
}

@Composable
fun LibraryItem(story: DemoStorys) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {

        Row(
            modifier = Modifier.padding(12.dp)
        ) {

            // Image
            AsyncImage(
                model = story.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Text Info
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = story.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = story.author,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}