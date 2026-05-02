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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sonozone.Loading.StorySkeletonCard
import com.example.sonozone.MostSearchStoriesStoriesViewModel
import com.example.sonozone.Story

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

@Composable
fun SearchScreen(
    navController: NavController,
) {
    var query by remember { mutableStateOf("") }


    val viewModel : MostSearchStoriesStoriesViewModel = viewModel()
    val stories = viewModel.SearchStoriesList.value
    val isLoading = viewModel.Searchloading.value

    LaunchedEffect(Unit) {
        if (stories.isEmpty()) {
            viewModel.getTopStories()
        }
    }


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
                        .clickable { viewModel.SearchStories(query) }
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
                        .clickable { viewModel.SearchStories(cat) }
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
        StoriesGrid(stories, navController , isLoading)
    }
}

// Stories grid
@Composable
private fun StoriesGrid(stories: List<Story>, navController: NavController, isLoading: Boolean) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        if (isLoading) {
            // Show 3-4 rows of skeletons while loading
            repeat(4) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StorySkeletonCard() // Make sure this uses Modifier.weight(1f)
                    StorySkeletonCard()
                }
            }
        }else if(stories.isEmpty()){
            // stories not found
            Text("No stories found", color = TextMuted)
        } else {
            // Show actual data
            stories.chunked(2).forEach { rowStories ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowStories.forEach { story ->
                        StoryCard(
                            story = story,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate("player/${story._id}") }
                        )
                    }
                    if (rowStories.size == 1) Spacer(Modifier.weight(1f))
                }
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
                text = story.title?:"",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = story.writer?:"",
                fontSize = 11.sp,
                color = TextMuted,
                maxLines = 1
            )
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                MiniPill("${story.duration}:00",  Accent, BgPill)
                MiniPill("${formatCount(story.status?.likes?:0)} Likes", Color.White, AccentBg)
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
