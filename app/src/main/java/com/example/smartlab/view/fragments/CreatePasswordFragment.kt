package com.example.smartlab.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartlab.R
import com.example.smartlab.databinding.FragmentCreatePasswordBinding
import com.example.smartlab.utils.DataManager

class CreatePasswordFragment : Fragment() {

    private lateinit var binding: FragmentCreatePasswordBinding

    private val TAG = this::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        val code = StringBuilder(4)
        binding.gridLayoutPasswordKeyboard.iterator().forEach { child ->
            if (child is TextView) {
                child.setOnClickListener {
                    if (code.length < 4) {
                        code.append(child.text)
                        updatePasswordIndicators(code.length)
                        Log.d(TAG, "setListeners: code - $code")
                        if (code.length == 4) {
                            DataManager.encryptPassword(code.toString())
                            findNavController().navigate(R.id.action_createPasswordFragment_to_patientCardFragment)
                        }
                    }
                }
            }
            if (child is ImageView) {
                child.setOnClickListener {
                    if (code.isNotEmpty()) {
                        code.deleteCharAt(code.lastIndex)
                        Log.d(TAG, "setListeners: code - $code")
                        updatePasswordIndicators(code.length)
                    }
                }
            }
        }
        binding.tvSkip.setOnClickListener {
            findNavController().navigate(R.id.action_createPasswordFragment_to_patientCardFragment)
        }
    }

    private fun updatePasswordIndicators(codeLength: Int) {
        clearIndicators()
        when (codeLength) {
            1 -> {
                binding.ivPassword1.setImageResource(R.drawable.selected_indicator)
            }
            2 -> {
                binding.ivPassword1.setImageResource(R.drawable.selected_indicator)
                binding.ivPassword2.setImageResource(R.drawable.selected_indicator)
            }
            3 -> {
                binding.ivPassword1.setImageResource(R.drawable.selected_indicator)
                binding.ivPassword2.setImageResource(R.drawable.selected_indicator)
                binding.ivPassword3.setImageResource(R.drawable.selected_indicator)
            }
            4 -> {
                binding.ivPassword1.setImageResource(R.drawable.selected_indicator)
                binding.ivPassword2.setImageResource(R.drawable.selected_indicator)
                binding.ivPassword3.setImageResource(R.drawable.selected_indicator)
                binding.ivPassword4.setImageResource(R.drawable.selected_indicator)
            }
        }
    }

    private fun clearIndicators() {
        binding.passwordIndicatorsContainer.iterator().forEach { child ->
            (child as ImageView).setImageResource(R.drawable.unselected_indicator)
        }
    }
}