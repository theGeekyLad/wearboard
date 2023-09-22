package com.thegeekylad.wearboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.thegeekylad.wearboard.ui.wearboard_theme.WearboardTheme
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@ExperimentalMaterial3Api
class WearboardAlertActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val inputPhoneText = remember { mutableStateOf("") }

            // A surface container using the 'background' color from the theme
            WearboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                AlertDialog(
                    title = {
                        Text(
                            text = intent.getStringExtra("dialog_title")!!,
                        )
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

                                finish()
                            }
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                finish()
                            }
                        ) {
                            Text("Dismiss")
                        }
                    }
                )
                }
            }
        }
    }
}