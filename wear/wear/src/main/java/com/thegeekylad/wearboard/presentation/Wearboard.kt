package com.thegeekylad.wearboard.presentation

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text

@Composable
fun Wearboard(
    onClickPhone: () -> Unit?,
    onClickWatch: () -> Unit?,
    isPhonePriority: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose input\nmethod",
                textAlign = TextAlign.Center,
            )
        }
        Chip(
            modifier = Modifier.width(100.dp),
            label = { TextItem("Phone") },
            colors = if (isPhonePriority) ChipDefaults.primaryChipColors() else ChipDefaults.secondaryChipColors(),
            onClick = { onClickPhone() }
        )
        Chip(
            modifier = Modifier.width(100.dp),
            label = { TextItem("Watch") },
            colors = if (!isPhonePriority) ChipDefaults.primaryChipColors() else ChipDefaults.secondaryChipColors(),
            onClick = { onClickWatch() }
        )
    }
}