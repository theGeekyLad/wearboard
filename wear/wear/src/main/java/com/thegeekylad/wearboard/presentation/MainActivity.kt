/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.thegeekylad.wearboard.presentation

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.AutoCenteringParams
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.input.RemoteInputIntentHelper
import com.thegeekylad.wearboard.R
import com.thegeekylad.wearboard.presentation.theme.WearboardTheme
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val label = remember { mutableStateOf("Search...") }
            val launcher =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    it.data?.let { data ->
                        val results: Bundle = RemoteInput.getResultsFromIntent(data)
                        val searchText: CharSequence? = results.getCharSequence("search_text")
                        label.value = searchText as String
                    }
                }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WearboardMaster(
                    context = applicationContext,
                    inputField = { onClickField, value ->
                        Chip(
                            modifier = Modifier.fillMaxWidth(1f),
                            label = { TextItem(value) },
                            contentPadding = ChipDefaults.CompactChipContentPadding,
                            onClick = onClickField
                        )
                    },
                    inputValue = label,
                    dialogTitle = "Search",
                    onClickWatch = {
                        val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent();
                        val remoteInputs: List<RemoteInput> = listOf(
                            RemoteInput.Builder("search_text")
                                .setLabel("Search Text")
                                .build()
                        )

                        RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)

                        launcher.launch(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun WearApp(greetingName: String) {

    WearboardTheme {
        /* If you have enough items in your list, use [ScalingLazyColumn] which is an optimized
         * version of LazyColumn for wear devices with some added features. For more information,
         * see d.android.com/wear/compose.
         */
        Greeting("Android")
    }
}

@Composable
fun TextItem(contents: String) {
    Text(
        modifier = Modifier,
        textAlign = TextAlign.Center,
        text = contents
    )
}

@Composable
fun Greeting(greetingName: String) {
    Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            text = stringResource(R.string.hello_world, greetingName)
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}

@Composable
fun TextInput() {
    val label = remember { mutableStateOf("Start")}
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                val results: Bundle = RemoteInput.getResultsFromIntent(data)
                val ipAddress: CharSequence? = results.getCharSequence("ip_address")
                label.value = ipAddress as String
            }
        }
    Column() {
        Chip(
            label = { Text(label.value) },
            onClick = {}
        )
        Chip(
            label = { Text("Search with specific IP") },
            onClick = {
                val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent();
                val remoteInputs: List<RemoteInput> = listOf(
                    RemoteInput.Builder("ip_address")
                        .setLabel("Manual IP Entry")
                        .build()
                )

                RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)

                launcher.launch(intent)
            }
        )
    }
}