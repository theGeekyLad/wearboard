package com.thegeekylad.wearboard.presentation

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@ExperimentalSerializationApi
@Composable
fun WearboardMaster(
    context: Context,
    inputField: @Composable (onClickField: () -> Unit, value: String) -> Unit,
    inputValue: MutableState<String>,
    onClickWatch: () -> Unit,
) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                if (!data.getBooleanExtra("isSelectionPhone", false))
                    onClickWatch()
                else {
                    val wearableMessagingManager = WearableMessagingManager.getInstance(context)

                    wearableMessagingManager.attachMessageListener(Constants.MSG_TYPE_INPUT) { byteArray ->
                        inputValue.value = ProtoBuf.decodeFromByteArray<WearboardPayload>(byteArray).data
                    }

                    wearableMessagingManager.ping(Constants.MSG_TYPE_INPUT, null)
                }
            }
        }

    inputField(
        {
            launcher.launch(Intent(context, WearboardActivity::class.java))
        },
        inputValue.value
    )
}