package com.example.sonozone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val demoStories = listOf(
        DemoStory("1", "The Lost Kingdom", "Sarah Johnson", "https://picsum.photos/300/200", "15:30", 1234, 567),
        DemoStory("2", "Midnight Dreams", "Michael Chen", "https://picsum.photos/300/201", "8:45", 892, 234),
        DemoStory("3", "Ocean's Secret", "Emma Wilson", "https://picsum.photos/300/202", "22:10", 2456, 890)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Shono Zone",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EE)
            )

            OutlinedButton(onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = "Search")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Search")
            }
        }

        StorySection("Top Stories", "Most popular", demoStories)

        Spacer(modifier = Modifier.height(80.dp))
    }
}


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