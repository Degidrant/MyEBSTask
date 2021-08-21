package com.flexeiprata.androidmytaskapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.flexeiprata.androidmytaskapplication.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavGraph()
        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    private fun setNavGraph() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
    }


}