package com.thegeekylad.wearboard

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import com.thegeekylad.wearboard.ui.theme.WearboardTheme
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val showWatchInput = mutableStateOf(false)

        setContent {
            val inputPhoneText = remember { mutableStateOf("") }

            WearboardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showWatchInput.value)
                        AlertDialog(
                            title = {
                                Row {
                                    Text(
                                        text = "Wearboard",
                                        fontStyle = FontStyle.Italic
                                    )
                                    Text(
                                        text = " | ",
                                    )
                                    Text(
                                        text = "Input",
                                    )
                                }
                            },
                            text = {
                                OutlinedTextField(
                                    value = inputPhoneText.value,
                                    onValueChange = { inputPhoneText.value = it },
                                    label = { Text("Placeholder") }
                                )
                            },
                            onDismissRequest = {

                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        val payload = WearboardPayload(inputPhoneText.value)
                                        val data = ProtoBuf.encodeToByteArray(payload)

                                        WearableMessagingManager
                                            .getInstance(applicationContext)
                                            .ping(Constants.MSG_TYPE_INPUT, data)

                                        showWatchInput.value = false
                                    }
                                ) {
                                    Text("Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        showWatchInput.value = false
                                    }
                                ) {
                                    Text("Dismiss")
                                }
                            }
                        )
                }
            }
        }

        val wearableMessagingManager = WearableMessagingManager.getInstance(applicationContext)

        wearableMessagingManager.attachMessageListener(Constants.MSG_TYPE_INPUT) { byteArray ->
            Log.e(Constants.APP_TAG, "Got a message!")
            showWatchInput.value = true
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WearboardTheme {
        Greeting("Android")
    }
}