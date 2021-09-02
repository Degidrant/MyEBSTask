package com.flexeiprata.androidmytaskapplication.utils

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class RequestPermissionsHelper {
    companion object {
        fun requestInstance(
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
    }
}