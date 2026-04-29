package com.example.sonozone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.sonozone.Story
import com.example.sonozone.TopStoriesViewModel

private val Accent = Color(0xFFA78BFA)

private fun formatCount(n: Int): String {
    return if (n >= 1000) "${"%.1f".format(n / 1000.0)}K" else n.toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: TopStoriesViewModel = viewModel()

    val popularList = viewModel.allTimeList.value
    val weekList = viewModel.weekList.value
    val monthList = viewModel.monthList.value
    val yearList = viewModel.yearList.value

    val listState = rememberLazyListState()

    // first load
    LaunchedEffect(Unit) {
        if (popularList.isEmpty()) {
            viewModel.getTopStories("allTime")
        }
    }

    // load on scroll
    LaunchedEffect(listState.firstVisibleItemIndex) {

        when (listState.firstVisibleItemIndex) {

            1 -> {
                if (weekList.isEmpty()) {
                    viewModel.getTopStories("week")
                }
            }

            2 -> {
                if (monthList.isEmpty()) {
                    viewModel.getTopStories("month")
                }
            }

            3 -> {
                if (yearList.isEmpty()) {
                    viewModel.getTopStories("year")
                }
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D10))
    ) {

        item {
            Header()
        }

        item {
            Banner(popularList)
        }

        item {
            StorySection("Popular Stories", popularList)
        }

        item {
            StorySection("Top Stories This Week", weekList)
        }

        item {
            StorySection("Top Stories This Month", monthList)
        }

        item {
            StorySection("Top Stories This Year", yearList)
        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun Header() {

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
            color = Color.White
        )

        OutlinedButton(onClick = {}) {

            Icon(Icons.Default.Search, null)

            Spacer(modifier = Modifier.width(4.dp))

            Text("Search")
        }
    }
}

@Composable
fun Banner(list: List<Story>) {

    if (list.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Accent)
        }
        return
    }

    val story = list.first()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Box {

            AsyncImage(
                model = story.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {

                Text(
                    text = "Featured Story",
                    color = Color.White,
                    fontSize = 12.sp
                )

                Text(
                    text = story.title ?: "",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 2
                )

                Text(
                    text = "by ${story.writer}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun StorySection(
    title: String,
    stories: List<Story>
) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            TextButton(onClick = {}) {
                Text("See all")
            }
        }

        if (stories.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Accent)
            }

        } else {

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
}

@Composable
fun StoryCard(story: Story) {

    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp)
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

            Column(
                modifier = Modifier.padding(6.dp)
            ) {

                Text(
                    text = story.title ?: "Untitled",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "by ${story.writer}",
                    fontSize = 9.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    MiniPill(
                        "${story.duration}:00",
                        Accent,
                        Color.Black
                    )

                    MiniPill(
                        "${formatCount(story.status?.likes ?: 0)} likes",
                        Color.White,
                        Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun MiniPill(
    text: String,
    textColor: Color,
    bgColor: Color
) {

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {

        Text(
            text = text,
            fontSize = 10.sp,
            color = textColor
        )
    }
}
