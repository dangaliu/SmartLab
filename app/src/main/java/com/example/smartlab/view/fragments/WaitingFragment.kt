package com.example.smartlab.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.smartlab.databinding.FragmentWaitingBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WaitingFragment : Fragment() {

    private lateinit var binding: FragmentWaitingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWaitingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            binding.waitingContainer.visibility = View.VISIBLE
            binding.mainContainer.visibility = View.GONE
            delay(3000)
            binding.tvWaiting.text = "Производим оплату..."
            delay(2000)
            binding.waitingContainer.visibility = View.GONE
            binding.mainContainer.visibility = View.VISIBLE
        }
    }
}