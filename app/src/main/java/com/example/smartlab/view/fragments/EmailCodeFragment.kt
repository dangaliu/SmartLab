package com.example.smartlab.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smartlab.databinding.FragmentEmailCodeBinding

class EmailCodeFragment : Fragment() {

    private lateinit var binding: FragmentEmailCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEmailCodeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}