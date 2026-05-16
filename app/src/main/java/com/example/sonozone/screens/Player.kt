package com.example.sonozone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sonozone.AudioViewModel
import com.example.sonozone.RecommentStoriesViewModel
import com.example.sonozone.Story
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ── Tokens ────────────────────────────────────────────────────
private val BgPage      = Color(0xFF0D0D10)
private val BgCard      = Color(0xFF18171F)
private val BgChip      = Color(0xFF26242E)
private val Accent      = Color(0xFFA78BFA)
private val AccentBg    = Color(0x1FA78BFA)
private val TextPrimary   = Color(0xFFF0EEF8)
private val TextSecondary = Color(0xFF9E9CAC)
private val TextMuted     = Color(0xFF7A788A)
private val LikeRed       = Color(0xFFFF6B8A)



@Composable
fun PlayerScreen(storyId: String, navController: NavController) {

    println("storyId: $storyId")

    //Relet Story fetched from the API
    val reletViewModel: RecommentStoriesViewModel = viewModel()
    val related = reletViewModel.RecommentStoriesList.value
    val isReletloading = reletViewModel.Recommentloading.value

    LaunchedEffect(storyId) {
        reletViewModel.getRecommentStories(storyId)
    }



    //Audio Fetched from the API
    val viewModel: AudioViewModel = viewModel()
    val story = viewModel.selectedAudio.value
    val isAudioLoading = viewModel.isAudioLoading.value
    LaunchedEffect(storyId) {
        viewModel.fetchAudio(storyId)
    }

    val context = LocalContext.current

    var selectedLang by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(story) {
        selectedLang = when {
            !story?.audio?.bangla?.url.isNullOrEmpty() -> "bangla"
            !story?.audio?.english?.url.isNullOrEmpty() -> "english"
            !story?.audio?.arabic?.url.isNullOrEmpty() -> "arabic"
            else -> null
        }
    }

    val audioUrl = when (selectedLang) {
        "bangla" -> story?.audio?.bangla?.url
        "english" -> story?.audio?.english?.url
        "arabic" -> story?.audio?.arabic?.url
        else -> null
    }



    // ExoPlayer
    val player = remember {
        ExoPlayer.Builder(context).build()
    }

    var isPlaying by remember { mutableStateOf(false) }
    var isBuffering by remember { mutableStateOf(false) }
    var isPrepared by remember { mutableStateOf(false) }
    var currentMs      by remember { mutableLongStateOf(0L) }
    var durationMs     by remember { mutableLongStateOf(1L) }
    var isLiked        by remember { mutableStateOf(false) }
    var isSeeking by remember { mutableStateOf(false) }


    // Progress polling
    LaunchedEffect(player) {
        while (true) {
            // Only update currentMs if the user is NOT dragging the slider
            if (!isSeeking && player.isPlaying) {
                currentMs = player.currentPosition
            }

            val dur = player.duration
            if (dur > 0) durationMs = dur

            delay(200) // Lower delay means smoother slider movement
        }
    }

    // Reload on lang change
    LaunchedEffect(audioUrl) {
        if (audioUrl.isNullOrEmpty()) return@LaunchedEffect

        val newUri = MediaItem.fromUri(audioUrl)
        val currentUri = player.currentMediaItem?.localConfiguration?.uri?.toString()

        if (currentUri == audioUrl) return@LaunchedEffect

        player.stop()
        player.clearMediaItems()
        player.setMediaItem(newUri)
        player.prepare()

        isPlaying = false
        currentMs = 0L
    }


    LaunchedEffect(player) {
        player.addListener(object : androidx.media3.common.Player.Listener {

            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    androidx.media3.common.Player.STATE_BUFFERING -> {
                        isBuffering = true
                    }

                    androidx.media3.common.Player.STATE_READY -> {
                        isBuffering = false
                        isPrepared = true
                    }

                    androidx.media3.common.Player.STATE_ENDED -> {
                        isPlaying = false
                    }
                }
            }
        })
    }

    // Fix: Pass 'player' as the key so it re-triggers if the player instance changes
    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPage)
            .verticalScroll(rememberScrollState())
    ) {

        // ── Cover art ──────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            AsyncImage(
                model = story?.imageUrl,
                contentDescription = story?.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // gradient fade into page bg
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, BgPage),
                            startY = 140f
                        )
                    )
            )
        }

        // ── Story info ─────────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {

            Text(
                text = story?.title?:"",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text("Writer: ${story?.writer}", fontSize = 13.sp, color = TextSecondary)
            Text("Narrated by ${story?.writer}", fontSize = 12.sp, color = TextMuted)

            Spacer(Modifier.height(20.dp))

            // ── Progress ───────────────────────────────────────
            val scope = rememberCoroutineScope()
            Slider(
                value = currentMs.toFloat(),
                onValueChange = {
                    isSeeking = true
                    currentMs = it.toLong()
                },
                onValueChangeFinished = {
                    player.seekTo(currentMs)
                    // Launch a coroutine to handle the settling delay
                    scope.launch {
                        delay(150) // Wait 150ms for ExoPlayer to settle on the new position
                        isSeeking = false
                    }
                },
                valueRange = 0f..durationMs.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Accent,
                    activeTrackColor = Accent,
                    inactiveTrackColor = Color.White.copy(alpha = 0.15f)
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatMs(currentMs), fontSize = 11.sp, color = TextMuted)
                Text(formatMs(durationMs), fontSize = 11.sp, color = TextMuted)
            }

            Spacer(Modifier.height(20.dp))

            // ── Playback controls ──────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Skip back 30s
                SkipButton(label = "-30") {
                    player.seekTo((player.currentPosition - 30_000).coerceAtLeast(0))
                }

                // Play / Pause
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Accent)
                        .clickable {
                        if (isPlaying) {
                            player.pause()
                            isPlaying = false
                        } else {
                            if (player.playbackState == androidx.media3.common.Player.STATE_IDLE) {
                                player.prepare()
                            }
                            player.play()
                            isPlaying = true
                        }
                    },
                    contentAlignment = Alignment.Center
                ) {
                    if (isBuffering) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Star else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                // Skip forward 30s
                SkipButton(label = "+30") {
                    player.seekTo(
                        (player.currentPosition + 30_000).coerceAtMost(durationMs)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ── Action chips ───────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Like
                ActionChip(
                    icon = if (isLiked) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                    label = if (isLiked) "Liked" else "Like",
                    tint = if (isLiked) LikeRed else TextSecondary,
                    bgColor = if (isLiked) LikeRed.copy(alpha = 0.12f) else BgChip,
                    onClick = { isLiked = !isLiked }
                )
                ActionChip(
                    icon = Icons.Outlined.AddCircle,
                    label = "Comment",
                    onClick = {}
                )
                ActionChip(
                    icon = Icons.Default.Share,
                    label = "Share",
                    onClick = {}
                )
            }

            Spacer(Modifier.height(10.dp))

            //  Language selector
            val langs = buildList {
                if (!story?.audio?.bangla?.url.isNullOrEmpty()) add("bangla")
                if (!story?.audio?.english?.url.isNullOrEmpty()) add("english")
                if (!story?.audio?.arabic?.url.isNullOrEmpty()) add("arabic")
            }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    langs.forEach { lang ->
                        val active = selectedLang == lang
                        println(lang)
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (active) Accent else BgChip)
                                .clickable { selectedLang = lang }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = lang.replaceFirstChar { it.uppercase() },
                                fontSize = 12.sp,
                                color = if (active) Color.White else TextSecondary
                            )
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))

            // ── Read button ────────────────────────────────────
            OutlinedButton(
                onClick = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Accent)
            ) {
                Icon(Icons.Default.Menu, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text("Read Story Text", fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(28.dp))

        // ── Recommended ────────────────────────────────────────
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text("Recommended for You", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(Modifier.height(2.dp))
            Text("Based on your listening history", fontSize = 12.sp, color = TextMuted)
            Spacer(Modifier.height(14.dp))

            related.chunked(2).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    row.forEach { s ->
                        RelatedCard(
                            story = s,
                            modifier = Modifier.weight(1f),
                            onClick = {  }
                        )
                    }
                    if (row.size == 1) Spacer(Modifier.weight(1f))
                }
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}

// ── Related card ──────────────────────────────────────────────
@Composable
private fun RelatedCard(
    story: Story,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BgCard)
            .clickable(onClick = onClick)
    ) {
        Box {
            AsyncImage(
                model = story.imageUrl,
                contentDescription = story.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )
            // Duration badge
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text("${story.duration}:00", fontSize = 10.sp, color = Color.White)
            }
        }
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                story?.title?:"",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(story.writer?:"", fontSize = 10.sp, color = TextMuted, maxLines = 1)
            Spacer(Modifier.height(4.dp))
            Text(
                "${formatCount(story.status?.likes?:0)} views • ${formatCount(story.status?.likes?:0)} likes",
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}

// ── Skip button ───────────────────────────────────────────────
@Composable
private fun SkipButton(label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(BgChip)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = if (label.startsWith("-")) Icons.Default.Refresh else Icons.Default.ArrowForward,
            contentDescription = label,
            tint = TextSecondary,
            modifier = Modifier.size(20.dp)
        )
        Text(label, fontSize = 10.sp, color = TextMuted)
    }
}

// ── Action chip ───────────────────────────────────────────────
@Composable
private fun ActionChip(
    icon: ImageVector,
    label: String,
    tint: Color = TextSecondary,
    bgColor: Color = BgChip,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(14.dp))
        Text(label, fontSize = 12.sp, color = tint)
    }
}

// ── Helpers ───────────────────────────────────────────────────
private fun formatMs(ms: Long): String {
    val total = (ms / 1000).toInt()
    val m = total / 60
    val s = total % 60
    return "$m:${s.toString().padStart(2, '0')}"
}

private fun formatCount(n: Int) =
    if (n >= 1000) "${"%.1f".format(n / 1000.0)}K" else n.toString()

