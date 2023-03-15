package com.example.smartlab.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.smartlab.databinding.FragmentEmailCodeBinding
import com.example.smartlab.utils.GenericKeyEvent
import com.example.smartlab.utils.GenericTextWatcher

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCodeEditors()
    }

    private fun setupCodeEditors() {
        with(binding) {
            etCode1.requestFocus()
            etCode1.addTextChangedListener(GenericTextWatcher(etCode1, etCode2))
            etCode2.addTextChangedListener(GenericTextWatcher(etCode2, etCode3))
            etCode3.addTextChangedListener(GenericTextWatcher(etCode3, etCode4))
            etCode4.addTextChangedListener(
                GenericTextWatcher(
                    etCode4,
                    null,
                    onLastEditTextFilled = {
                        val imm =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        view?.let {
                            imm.hideSoftInputFromWindow(it.windowToken, 0)
                        }
                    })
            )

            etCode1.setOnKeyListener(GenericKeyEvent(etCode1, null))
            etCode2.setOnKeyListener(GenericKeyEvent(etCode2, etCode1))
            etCode3.setOnKeyListener(GenericKeyEvent(etCode3, etCode2))
            etCode4.setOnKeyListener(GenericKeyEvent(etCode4, etCode3))
        }
    }
}