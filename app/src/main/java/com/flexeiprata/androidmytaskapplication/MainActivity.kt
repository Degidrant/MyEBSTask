package com.flexeiprata.androidmytaskapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.flexeiprata.androidmytaskapplication.common.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.common.PRODUCT_ID
import com.flexeiprata.androidmytaskapplication.databinding.MainActivityBinding
import com.flexeiprata.androidmytaskapplication.products.presentation.views.MainFragmentDirections
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavGraph()
        setFirebase()
        checkNotifications()
    }

    private fun checkNotifications() {
        intent.extras?.let {bundle ->
            val rawCID = bundle.getSerializable(PRODUCT_ID)
            if (rawCID != null) {
                val cID = rawCID.toString().toInt()
                navHostFragment.navController.navigate(MainFragmentDirections.actionMainFragmentToDescFragment(cID))
            }

        }
    }

    private fun setFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(LOG_DEBUG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
        })

    }

    private fun setNavGraph() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
    }

}