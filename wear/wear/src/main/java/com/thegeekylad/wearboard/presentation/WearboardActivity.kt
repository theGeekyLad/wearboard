package com.thegeekylad.wearboard.presentation

import android.app.Activity
import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.wear.input.RemoteInputIntentHelper
import com.thegeekylad.wearboard.presentation.theme.WearboardTheme

class WearboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearboardTheme {
                /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
                 * version of LazyColumn for wear devices with some added features. For more information,
                 * see d.android.com/wear/compose.
                 */
                Wearboard(
                    activity = this,
                    onClickPhone = {
                        val intentResult = Intent()
                        intentResult.putExtra("isSelectionPhone", true)
                        setResult(RESULT_OK, intentResult)
                        finish()
                    },
                    onClickWatch = {
                        val intentResult = Intent()
                        intentResult.putExtra("isSelectionPhone", false)
                        setResult(RESULT_OK, intentResult)
                        finish()
                    }
                )
            }
        }
    }
}