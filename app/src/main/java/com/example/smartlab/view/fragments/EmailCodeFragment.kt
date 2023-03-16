package com.example.smartlab.view.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.smartlab.databinding.FragmentEmailCodeBinding
import com.example.smartlab.utils.GenericKeyEvent
import com.example.smartlab.utils.GenericTextWatcher
import com.example.smartlab.utils.SaveStatus
import com.example.smartlab.viewmodel.EmailCodeViewModel
import kotlin.math.round

class EmailCodeFragment : Fragment() {

    private lateinit var binding: FragmentEmailCodeBinding

    private val viewModel: EmailCodeViewModel by viewModels()

    private val TAG = this::class.simpleName

    private lateinit var timer: CountDownTimer

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
        viewModel.getEmail()
        setupCodeEditors()
        setListeners()
        setObservers()
        initTimer()
        viewModel.startTimer(timer)
    }

    private fun setListeners() {
        binding.ivBtnBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun initTimer() {
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTimer.text =
                    "Отправить код повторно можно будет через ${round(millisUntilFinished.toFloat() / 1000).toInt()} секунд"
            }

            override fun onFinish() {
                viewModel.stopTimer(timer)
                viewModel.startTimer(timer)
                viewModel.sendCode(viewModel.email.value ?: "")
            }
        }
    }

    private fun setObservers() {
        viewModel.signInStatus.observe(viewLifecycleOwner) {
            Log.d(TAG, "setObservers: signInStatus - $it ")
            if (it == SaveStatus.SUCCESS) {
                timer.cancel()
                viewModel.clearSignInStatus()
            }
        }
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
                        etCode4.clearFocus()
                        val imm =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        view?.let {
                            imm.hideSoftInputFromWindow(it.windowToken, 0)
                        }
                        val code = binding.etCode1.text.toString() +
                                binding.etCode2.text.toString() +
                                binding.etCode3.text.toString() +
                                binding.etCode4.text.toString()
                        viewModel.signIn(viewModel.email.value ?: "", code)
                    })
            )

            etCode1.setOnKeyListener(GenericKeyEvent(etCode1, null))
            etCode2.setOnKeyListener(GenericKeyEvent(etCode2, etCode1))
            etCode3.setOnKeyListener(GenericKeyEvent(etCode3, etCode2))
            etCode4.setOnKeyListener(GenericKeyEvent(etCode4, etCode3))
        }
    }
}