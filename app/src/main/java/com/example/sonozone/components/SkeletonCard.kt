package com.example.sonozone.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


@Composable
fun StorySection(
    title: String,
    subtitle: String,
    stories: List<DemoStory>
) {
    Column {

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            stories.forEach {
                StoryCard(it)
            }
        }
    }
}

@Composable
fun StoryCard(story: DemoStory) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
    ) {
        Column {
            AsyncImage(
                model = story.imageUrl,
                contentDescription = null,
                modifier = Modifier.height(140.dp),
                contentScale = ContentScale.Crop
            )

            Text(story.title, modifier = Modifier.padding(8.dp))
        }
    }
}

data class DemoStory(
    val id: String,
    val title: String,
    val author: String,
    val imageUrl: String,
    val duration: String,
    val views: Int,
    val likes: Int
)