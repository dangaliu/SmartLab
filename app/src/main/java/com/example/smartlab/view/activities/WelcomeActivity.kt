package com.example.smartlab.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smartlab.databinding.ActivityMainBinding
import com.example.smartlab.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}