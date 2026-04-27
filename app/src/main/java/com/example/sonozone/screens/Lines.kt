package com.example.sonozone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage

// ── Tokens ────────────────────────────────────────────────────
private val BgPage   = Color(0xFF0D0D10)
private val Accent   = Color(0xFFA78BFA)
private val TextPrimary   = Color(0xFFF0EEF8)
private val TextSecondary = Color(0xFF9E9CAC)

// ── Model ─────────────────────────────────────────────────────
data class Line(
    val id: String,
    val title: String,
    val writer: String,
    val imageUrl: String,
    val audioUrl: String,
    val likes: Int = 0,
    val comments: Int = 0
)

// ── Screen ────────────────────────────────────────────────────
@Composable
fun LinesScreen() {

    // Replace with your ViewModel / API call
    val lines = remember { sampleLines() }

    val pagerState = rememberPagerState { lines.size }

    Box(modifier = Modifier.fillMaxSize().background(BgPage)) {

        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            LineCard(
                line = lines[page],
                isActive = pagerState.currentPage == page
            )
        }

        // ── Header overlay ─────────────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 52.dp)
                .zIndex(10f)
        ) {
            Text(
                text = "Lines",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Scroll • Listen • Feel",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }
    }
}

// ── Single card ───────────────────────────────────────────────
@Composable
fun LineCard(line: Line, isActive: Boolean) {

    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var duration by remember { mutableFloatStateOf(1f) }
    var liked by remember { mutableStateOf(false) }

    // ExoPlayer setup
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(line.audioUrl))
            prepare()
        }
    }

    // Play/pause when page becomes active
    LaunchedEffect(isActive) {
        if (isActive) {
            player.play()
            isPlaying = true
        } else {
            player.pause()
            player.seekTo(0)
            isPlaying = false
        }
    }

    // Progress polling
    LaunchedEffect(isActive) {
        if (!isActive) return@LaunchedEffect
        while (true) {
            val dur = player.duration.takeIf { it > 0 }?.toFloat() ?: 1f
            duration = dur
            progress = player.currentPosition.toFloat()
            kotlinx.coroutines.delay(500)
        }
    }

    // Release on dispose
    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        // Background image
        AsyncImage(
            model = line.imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    if (isPlaying) player.pause() else player.play()
                    isPlaying = !isPlaying
                }
        )

        // Dark gradient overlay (bottom)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color(0xCC0D0D10)),
                        startY = 400f
                    )
                )
        )

        // Play / Pause indicator (center, fades away feeling)
        if (!isPlaying) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text("▶", fontSize = 26.sp, color = Color.White)
            }
        }

        // Bottom info + actions
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 72.dp, bottom = 100.dp)
        ) {
            Text(
                text = line.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "by ${line.writer}",
                fontSize = 13.sp,
                color = TextSecondary
            )
            Spacer(Modifier.height(14.dp))

            // Progress bar
            Slider(
                value = progress,
                onValueChange = {
                    progress = it
                    player.seekTo(it.toLong())
                },
                valueRange = 0f..duration,
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Accent,
                    activeTrackColor = Accent,
                    inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                )
            )
        }

        // Action buttons (right side)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Like
            ActionButton(
                icon = {
                    Icon(
                        if (liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (liked) Color(0xFFFF6B8A) else TextPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = "${if (liked) line.likes + 1 else line.likes}",
                onClick = { liked = !liked }
            )

            // Comment
            ActionButton(
                icon = {
                    Icon(
                        Icons.Outlined.MailOutline,
                        contentDescription = "Comment",
                        tint = TextPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = "${line.comments}",
                onClick = {}
            )

            // Share
            ActionButton(
                icon = {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = "Share",
                        tint = TextPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = "Share",
                onClick = {}
            )
        }
    }
}

// ── Action button ─────────────────────────────────────────────
@Composable
private fun ActionButton(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        icon()
        Spacer(Modifier.height(3.dp))
        Text(label, fontSize = 11.sp, color = TextSecondary)
    }
}

// ── Sample data ───────────────────────────────────────────────
private fun sampleLines() = listOf(
    Line("1", "The Empty Road",   "Sarah Johnson", "https://picsum.photos/seed/l1/400/800", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3", 234, 18),
    Line("2", "Rain on Glass",    "Michael Chen",  "https://picsum.photos/seed/l2/400/800", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3", 891, 42),
    Line("3", "Fading Light",     "Emma Wilson",   "https://picsum.photos/seed/l3/400/800", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3", 156, 9),
    Line("4", "Still Waters",     "Aisha Rahman",  "https://picsum.photos/seed/l4/400/800", "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3", 310, 27),
)