package com.flexeiprata.androidmytaskapplication.common

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class RequestPermissionsHelper {
    companion object {
        fun requestInstanceDefault(
            context: Fragment,
            actionIfGranted: () -> Unit
        ) =
            context.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    actionIfGranted.invoke()
                } else {
                    Toast.makeText(
                        context.requireContext(),
                        "Sorry, it is unable without permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        fun requestInstanceWithElse(
            context: Fragment,
            actionIfGranted: () -> Unit,
            actionIfNotGranted: () -> Unit
        ) =
            context.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    actionIfGranted.invoke()
                } else {
                    actionIfNotGranted.invoke()
                }
            }

        fun requestPermission(
            context: Context,
            permission: String,
            requestPermissionLauncher: ActivityResultLauncher<String>,
            actionIfGranted: () -> Unit,
            actionIfNotGranted: () -> Unit = {requestPermissionLauncher.launch(permission)}
        ) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) -> {
                    actionIfGranted.invoke()
                }
                else -> {
                    actionIfNotGranted.invoke()
                }
            }
        }
    }
}