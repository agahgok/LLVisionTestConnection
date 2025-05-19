package com.vartech.llvisiontestconnection

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.llvision.glass3.platform.ConnectionStatusListener
import com.llvision.glass3.platform.IGlass3Device
import com.llvision.glass3.platform.LLVisionGlass3SDK
import com.llvision.glass3.platform.base.BasePermissionActivity
import com.llvision.glxss.common.service.GlassServiceManager
import com.vartech.llvisiontestconnection.ui.theme.LLVisionTestConnectionTheme

class MainActivity : BasePermissionActivity() {

    private var connectState by mutableStateOf("Device not connected")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkStorageManagerPermission(this)
        checkOverlayPermission()

        requestPermissions()

        setContent {
            LLVisionTestConnectionTheme {
                Scaffold { padding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = connectState,
                            color = if (connectState.contains("connected"))
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = { requestPermissions() }) {
                            Text("Re-request Permissions")
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!LLVisionGlass3SDK.getInstance().isServiceConnected) {
            LLVisionGlass3SDK.getInstance().init(this, glassStatus)
        }
    }

    override fun onPermissionAccepted(isAccepted: Boolean) {
        if (isAccepted) {
            LLVisionGlass3SDK.getInstance().init(this, glassStatus)
        } else {
            Toast.makeText(this, "Gerekli izinler verilmedi", Toast.LENGTH_LONG).show()
        }
    }

    private val glassStatus = object : ConnectionStatusListener {
        override fun onServiceConnected(devices: List<IGlass3Device>) {}
        override fun onServiceDisconnected() {}

        override fun onDeviceConnect(device: IGlass3Device) {
            runOnUiThread { connectState = "Device connected" }
        }
        override fun onDeviceDisconnect(device: IGlass3Device) {
            runOnUiThread { connectState = "Device disconnected" }
        }
        override fun onError(code: Int, msg: String) {
            runOnUiThread { connectState = "Error: $msg" }
        }
    }

    override fun onDestroy() {
        if (LLVisionGlass3SDK.getInstance().isServiceConnected) {
            LLVisionGlass3SDK.getInstance().destroy()
        }
        GlassServiceManager.stopAllServices()
        super.onDestroy()
    }

    private fun checkStorageManagerPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
            && !Environment.isExternalStorageManager()
        ) {
            Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
            }
        }
    }

    private fun checkOverlayPermission() {
        if (!Settings.canDrawOverlays(this)) {
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            ).also { startActivity(it) }
        }
    }
}
