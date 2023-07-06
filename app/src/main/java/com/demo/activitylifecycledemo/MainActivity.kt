package com.demo.activitylifecycledemo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.demo.activitylifecycledemo.ui.theme.ActivityLifecycleDemoTheme

class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = ">>>> MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate:")
        setContent {
            ActivityLifecycleDemoTheme {
                // A surface container using the 'background' color from the theme

                val showDialog = remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(showDialog, this@MainActivity)
                    SimpleAlertDialog(show = showDialog.value,
                        onDismiss = {
                            showDialog.value = false
                        },
                        onConfirm = {
                            showDialog.value = false
                        })
                }
            }
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "onRestoreInstanceState:")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState:")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart:")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume:")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause:")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop:")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy:")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart:")
    }

}

fun checkAndRequestCameraPermission(
    context: Context,
    permission: String,
    launcher: ManagedActivityResultLauncher<String, Boolean>
) {
    val permissionCheckResult = ContextCompat.checkSelfPermission(context, permission)
    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
        // Open camera because permission is already granted
    } else {
        // Request a permission
        launcher.launch(permission)
    }
}

@Composable
fun SimpleAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm() })
                { Text(text = "OK") }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() })
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Please confirm") },
            text = { Text(text = "Should I continue with the requested action?") }
        )
    }
}

@Composable
fun MainContent(showDialog: MutableState<Boolean>, mainActivity: MainActivity) {
    val context = LocalContext.current
    Column {

        Button(
            onClick = {
                showDialog.value = true
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Open Alert Dialog")
        }

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Open camera
                Log.d(MainActivity.TAG, "Camera permission already granted")
            } else {
                // Show dialog
                Log.d(MainActivity.TAG, "Ask for Camera permission")
            }
        }

        Button(
            onClick = {
                checkAndRequestCameraPermission(context, Manifest.permission.CAMERA, launcher)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Open Camera")
        }

        Button(
            onClick = {
                mainActivity.startActivity(Intent(mainActivity, SecondActivity::class.java))
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Open Second Activity")
        }

    }
}