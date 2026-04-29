package com.example.sonozone.Loading


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun StorySkeletonCard() {

    Card(
        modifier = Modifier
            .width(180.dp)
            .padding(5.dp),
        shape = RoundedCornerShape(10.dp)
    ) {

        Column {

            // image skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF2A2A2A))
            )

            Column(
                modifier = Modifier.padding(6.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(12.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFF2A2A2A))
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0xFF2A2A2A))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .width(55.dp)
                            .height(18.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2A2A2A))
                    )

                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .height(18.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2A2A2A))
                    )
                }
            }
        }
    }
}