package com.thegeekylad.wearboard

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException

class WearableMessagingManager(private val applicationContext: Context) : MessageClient.OnMessageReceivedListener {

    private val messageClient by lazy { Wearable.getMessageClient(applicationContext) }
    private val nodeClient by lazy { Wearable.getNodeClient(applicationContext) }
    private val mapListeners = HashMap<String, (byteArray: ByteArray) -> Unit>()

    companion object {
        private var wearableMessagingManager: WearableMessagingManager? = null

        fun getInstance(applicationContext: Context): WearableMessagingManager {
            if (wearableMessagingManager == null) {
                wearableMessagingManager = WearableMessagingManager(applicationContext)
            }
            return wearableMessagingManager as WearableMessagingManager
        }
    }

    init {
        messageClient.addListener(this)
    }

    fun ping(path: String, byteArray: ByteArray?) {
        Log.d(Constants.APP_TAG, "Sending message ...")
        GlobalScope.launch {
            try {
                val nodesList: List<Node> = Tasks.await(nodeClient.connectedNodes)
                if (nodesList.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        val node = nodesList[0]
                        Wearable
                            .getMessageClient(applicationContext)
                            .sendMessage(node.id, path, byteArray)
                            .addOnSuccessListener {
                                Log.d(Constants.APP_TAG, "Sent.")
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    applicationContext,
                                    "Failed: " + it.message,
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.d(Constants.APP_TAG, "Sending failed.")
                            }
                    }
                }
            } catch (e: ExecutionException) {
                Log.d(Constants.APP_TAG, "No watches paired to this device. Cannot send message.")
            }
        }
    }

    override fun onMessageReceived(p0: MessageEvent) {
        Log.d(Constants.APP_TAG, "Received a message: " + p0.path)
        try {
            mapListeners[p0.path]?.let { it(p0.data) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun attachMessageListener(type: String, callback: (byteArray: ByteArray) -> Unit) {
        mapListeners[type] = callback
    }
}