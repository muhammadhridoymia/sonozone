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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sonozone.Story
import com.example.sonozone.TopStoriesViewModel
import com.example.sonozone.Loading.StorySkeletonCard

private val Accent = Color(0xFFA78BFA)

private fun formatCount(n: Int): String {
    return if (n >= 1000) "${"%.1f".format(n / 1000.0)}K" else n.toString()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {

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
            Banner()
        }

        item {
            StorySection("Popular Stories", popularList,navController = navController)
        }

        item {
            StorySection("Top Stories This Week", weekList,navController = navController)
        }

        item {
            StorySection("Top Stories This Month", monthList,navController = navController)
        }

        item {
            StorySection("Top Stories This Year", yearList,navController = navController)
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
fun Banner() {
    // Dummy data for now (replace with real data)
    val url ="https://res.cloudinary.com/dyqmmzz5f/image/upload/v1776931178/listenBanner_tkoqye.png"

    if (url.isEmpty()) {
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


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Box {

            AsyncImage(
                model = url,
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
                    text = "Explore the world of stories",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun StorySection(
    title: String,
    stories: List<Story>,
    navController: NavController
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

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 8.dp)
            ) {

                repeat(4) {
                    StorySkeletonCard()
                }
            }

        } else {

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 8.dp)
            ) {
                stories.forEach {
                    StoryCard(it,navController=navController)
                }
            }
        }
    }
}

@Composable
fun StoryCard(story: Story,navController: NavController) {

    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp),
        //naviget to player screen
        onClick = { navController.navigate("player/${story._id}") }

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