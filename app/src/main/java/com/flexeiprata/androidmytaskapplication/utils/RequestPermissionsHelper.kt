package com.flexeiprata.androidmytaskapplication.utils

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
            actionIsGranted: () -> Unit
        ) =
            context.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    actionIsGranted.invoke()
                } else {
                    Toast.makeText(
                        context.requireContext(),
                        "Sorry, unable without permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        fun requestInstanceWithElse(
            context: Fragment,
            actionIsGranted: () -> Unit,
            actionIsNotGranted: () -> Unit
        ) =
            context.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    actionIsGranted.invoke()
                } else {
                    actionIsNotGranted.invoke()
                }
            }

        fun requestPermission(
            context: Context,
            permission: String,
            requestPermissionLauncher: ActivityResultLauncher<String>,
            actionIsGranted: () -> Unit,
            actionIsNotGranted: () -> Unit = {requestPermissionLauncher.launch(permission)}
        ) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) -> {
                    actionIsGranted.invoke()
                }
                else -> {
                    actionIsNotGranted.invoke()
                }
            }
        }
    }
}