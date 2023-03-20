package com.example.smartlab.view.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.smartlab.R
import com.example.smartlab.databinding.FragmentLoginBinding
import com.example.smartlab.utils.isEmailValid
import com.example.smartlab.utils.showToast
import com.example.smartlab.viewmodel.LoginViewModel
import com.example.smartlab.viewmodel.SendCodeStatus

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private val TAG = this::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setEmailEditTextTextWatcher()
        setListeners()
    }

    private fun setListeners() {
        binding.btnNext.setOnClickListener {
            Log.d(TAG, "setListeners: Enabled: ${it.isEnabled}")
            viewModel.sendCode(binding.etEmail.text.toString())
        }
    }

    private fun setObservers() {
        viewModel.sendCodeStatus.observe(viewLifecycleOwner) {
            Log.d(TAG, "setObservers: $it")
            when (it) {
                SendCodeStatus.SUCCESS -> {
                    viewModel.saveEmail(binding.etEmail.text.toString())
                    viewModel.clearSendCodeStatus()
                    findNavController().navigate(R.id.action_loginFragment_to_emailCodeFragment)
                }
                SendCodeStatus.FAIL -> {
                    requireContext().showToast("Ошибка при отправке кода")
                }
                SendCodeStatus.NOTHING -> {}
            }
        }
        viewModel.saveEmailStatus.observe(viewLifecycleOwner) {
            Log.d(TAG, "setObservers: SaveEmailStatus - ${it.name}")
        }
    }

    private fun setEmailEditTextTextWatcher() {
        binding.btnNext.isEnabled = false
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (isEmailValid(it.toString())) {
                        binding.btnNext.isEnabled = true
                        binding.btnNext.backgroundTintList = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.accent, null
                            )
                        )
                    } else {
                        binding.btnNext.isEnabled = false
                        binding.btnNext.backgroundTintList = ColorStateList.valueOf(
                            resources.getColor(
                                R.color.accent_inactive, null
                            )
                        )
                    }
                }
            }
        })
    }
}