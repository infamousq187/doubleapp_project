package com.doubletapp_hw.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InfoScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 56.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Трекер привычек",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}