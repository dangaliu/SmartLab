package com.example.smartlab.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartlab.R
import com.example.smartlab.databinding.FragmentOrderBinding
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n")
class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private val calendar = Calendar.getInstance()

    private val TAG = this::class.java.simpleName
    val dateSetListener: DatePickerDialog.OnDateSetListener by lazy {
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
            updateDateTime()
            TimePickerDialog(
                requireContext(),
                { view, hourOfDay, minute ->
                    val sdf = SimpleDateFormat("hh:mm", Locale("ru"))
                    binding.tvDateTime.text = " ${binding.tvDateTime.text} ${
                        sdf.format(Calendar.getInstance().apply {
                            set(Calendar.HOUR, hourOfDay)
                            set(Calendar.MINUTE, minute)
                        }.time)
                    }"
                },
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Locale.setDefault(Locale("ru"))
        setListeners()
    }

    private fun setListeners() {
        binding.ivBtnBack.setOnClickListener { findNavController().popBackStack() }
        binding.tvDateTime.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                R.style.ColorPickerTheme,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).apply {
                datePicker.minDate = System.currentTimeMillis()
            }.show()
        }
    }

    private fun updateDateTime() {
        var prefix = ""
        val myFormat =
            if (calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
                prefix = if (calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance()
                        .get(Calendar.DAY_OF_MONTH)
                ) {
                    "Сегодня, "
                } else if (calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance()
                        .get(Calendar.DAY_OF_MONTH) + 1
                ) {
                    "Завтра, "
                } else {
                    ""
                }
                "dd MMMM"
            } else {
                "dd MMMM yyyy"
            }
        val sdf = SimpleDateFormat(myFormat, Locale("ru"))
        binding.tvDateTime.text = "$prefix${sdf.format(calendar.time)}"
    }
}