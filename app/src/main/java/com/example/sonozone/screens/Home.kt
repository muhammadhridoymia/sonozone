package com.example.sonozone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

private val Accent        = Color(0xFFA78BFA)
private fun formatCount(n: Int): String = if (n >= 1000) "${"%.1f".format(n / 1000.0)}K" else n.toString()
data class DemoStory(
    val id: String,
    val title: String,
    val author: String,
    val imageUrl: String,
    val duration: String,
    val views: Int,
    val likes: Int
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    val demoStories = listOf(
        DemoStory("1", "The Lost Kingdom is found", "Sarah Johnson", "https://picsum.photos/300/200", "15:30", 1234, 1567),
        DemoStory("2", "Midnight Dreams", "Michael Chen", "https://picsum.photos/300/201", "8:45", 892, 234),
        DemoStory("3", "Ocean's Secret", "Emma Wilson", "https://picsum.photos/300/202", "22:10", 2456, 890)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D10))
            .verticalScroll(rememberScrollState())
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ShonoZone",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF0EEF8)
            )

            OutlinedButton(onClick = {}) {
                Icon(Icons.Default.Search, contentDescription = "Search")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Search")
            }
        }

        // Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box {

                // Image (background)
                AsyncImage(
                    model = demoStories[2].imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Optional dark overlay (for better text visibility)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )

                // Text on top
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Featured Story",
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    Text(
                        text = demoStories[2].title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        StorySection("Top Stories", demoStories)
        StorySection("Top Week", demoStories)
        StorySection("Top Month", demoStories)
        StorySection("Top Year", demoStories)

        Spacer(modifier = Modifier.height(80.dp))
    }
}


@Composable
fun StorySection(
    title: String,
    stories: List<DemoStory>
) {
    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFF0EEF8)
            )

            TextButton(onClick = {}) {
                Text("See all", fontSize = 12.sp)
            }
        }

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 8.dp)
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
            .width(180.dp)
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {

            AsyncImage(
                model = story.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(5.dp)) {

                Text(
                    text = story.title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow= TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    MiniPill("${story.duration}", Accent, Color.Black)
                    MiniPill( "${formatCount(story.views)} views", Color.White, Color.Black)
                }
            }
        }
    }
}

@Composable
private fun MiniPill(label: String, textColor: Color, bgColor: Color) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(bgColor)
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(label, fontSize = 10.sp, color = textColor)
    }
}
