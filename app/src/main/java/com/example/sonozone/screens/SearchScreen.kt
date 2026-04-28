package com.example.sonozone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
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


// Tokens (same as HomeScreen)
private val BgPage      = Color(0xFF0D0D10)
private val BgCard      = Color(0xFF18171F)
private val BgInput     = Color(0xFF1C1B24)
private val BgPill      = Color(0xFF26242E)
private val BorderColor = Color(0xFF26242E)
private val TextPrimary   = Color(0xFFF0EEF8)
private val TextSecondary = Color(0xFF9E9CAC)
private val TextMuted     = Color(0xFF7A788A)
private val Accent        = Color(0xFFA78BFA)
private val AccentBg      = Color(0x1FA78BFA)

private val categories = listOf(
    "moral story", "adventure", "love & romance", "motivational",
    "sad & emotional", "comedy", "fantasy", "educational",
    "horror", "islamic/moral", "folktale", "science fiction", "others"
)

private fun formatCount(n: Int): String =
    if (n >= 1000) "${"%.1f".format(n / 1000.0)}K" else n.toString()

private fun sampleStories() = listOf(
    Story("1", "The Lost Kingdom",  "Sarah Johnson", "https://picsum.photos/seed/s1/320/200", 15, 1234, 567),
    Story("2", "Midnight Dreams",   "Michael Chen",  "https://picsum.photos/seed/s2/320/201",  8,  892, 234),
    Story("3", "Ocean's Secret",    "Emma Wilson",   "https://picsum.photos/seed/s3/320/202", 22, 2456, 890),
    Story("4", "Silent Shores",     "Aisha Rahman",  "https://picsum.photos/seed/s4/320/203", 12, 3100, 412),
    Story("5", "The Red Thread",    "James Park",    "https://picsum.photos/seed/s5/320/204", 18, 1800, 330),
    Story("6", "Ember & Ash",       "Layla Torres",  "https://picsum.photos/seed/s6/320/205", 10,  990, 155),
)

// Dummy model (replace with your real one)
data class Story(
    val id: String,
    val title: String,
    val writer: String,
    val imageUrl: String,
    val duration: Int,
    val views: Int,
    val likes: Int
)

@Composable
fun SearchScreen(
    onStoryClick: (String) -> Unit = {}
) {
    var query by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Replace with your real ViewModel / API call
    val stories = remember { sampleStories() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPage)
    ) {
        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BgInput)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text("Search", fontSize = 14.sp, color = TextMuted)
                        }
                        innerTextField()
                    }
                )
            }

            if (query.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Accent)
                        .clickable { /* trigger search */ }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Text("Go", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }

        // Category chips
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 8.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val selected = query.equals(cat, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (selected) Accent else BgPill)
                        .clickable { query = cat }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = cat,
                        fontSize = 12.sp,
                        color = if (selected) Color.White else TextSecondary
                    )
                }
            }
        }
        StoriesGrid(stories, onStoryClick)
    }
}

// Stories grid
@Composable
private fun StoriesGrid(stories: List<Story>, onStoryClick: (String) -> Unit) {
    // Simple 2-column grid via Column + chunked rows
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Spacer(Modifier.height(0.dp))
        stories.chunked(2).forEach { rowStories ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowStories.forEach { story ->
                    StoryCard(
                        story = story,
                        modifier = Modifier.weight(1f),
                        onClick = { onStoryClick(story.id) }
                    )
                }
                // fill empty slot if odd number
                if (rowStories.size == 1) Spacer(Modifier.weight(1f))
            }
        }
        Spacer(Modifier.height(80.dp))
    }
}

//Story card
@Composable
private fun StoryCard(story: Story, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BgCard)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = story.imageUrl,
            contentDescription = story.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
        )

        Column(modifier = Modifier.padding(9.dp)) {
            Text(
                text = story.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = story.writer,
                fontSize = 11.sp,
                color = TextMuted,
                maxLines = 1
            )
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                MiniPill("${story.duration}:00",  Accent, BgPill)
                MiniPill("${formatCount(story.views)} views", Color.White, AccentBg)
            }
        }
    }
}



// Reusable pill
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
