package com.rmuhamed.params.app.provider.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.rmuhamed.params.app.provider.service.ProviderAppService

class StartProviderAppServiceAtBootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED == intent?.action) {
            Log.d(StartProviderAppServiceAtBootReceiver::class.java.name, "Boot completed")
            context?.startService(Intent(context, ProviderAppService::class.java))
        }
    }
}