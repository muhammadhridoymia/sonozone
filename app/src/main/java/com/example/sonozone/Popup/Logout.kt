package com.example.sonozone.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LogoutDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onLogout: () -> Unit
) {

    if (!showDialog) return

    AlertDialog(
        onDismissRequest = { onDismiss() },
        containerColor = Color(0xFF18171F),
        shape = RoundedCornerShape(16.dp),

        title = {
            Text(
                text = "Logout",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },

        text = {
            Text(
                text = "Are you sure you want to logout?",
                color = Color(0xFF9E9CAC),
                fontSize = 14.sp
            )
        },

        confirmButton = {
            TextButton(
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                ),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    onLogout()
                    onDismiss()
                }
            ) {
                Text(
                    "Logout",
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },

        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    "Cancel",
                    color = Color.Gray
                )
            }
        }
    )
}