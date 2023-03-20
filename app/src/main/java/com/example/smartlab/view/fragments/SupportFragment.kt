package com.example.smartlab.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartlab.databinding.FragmentAnalyzesBinding
import com.example.smartlab.databinding.FragmentSupportBinding

class SupportFragment: Fragment() {

    private lateinit var binding: FragmentSupportBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSupportBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}