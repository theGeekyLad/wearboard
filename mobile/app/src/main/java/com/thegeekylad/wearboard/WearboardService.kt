package com.thegeekylad.wearboard

import android.app.AlertDialog
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.protobuf.ProtoBuf

@ExperimentalSerializationApi
@ExperimentalMaterial3Api
class WearboardService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val wearableMessagingManager = WearableMessagingManager.getInstance(applicationContext)

        wearableMessagingManager.attachMessageListener(Constants.MSG_TYPE_INPUT) { byteArray ->
            Log.e(Constants.APP_TAG, "Got a message!")

            val intent = Intent(applicationContext, WearboardAlertActivity::class.java)
            intent.putExtra("dialog_title", ProtoBuf.decodeFromByteArray<WearboardPayload>(byteArray).data)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}