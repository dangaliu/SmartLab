package com.example.smartlab.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartlab.R
import com.example.smartlab.databinding.BottomSheetAddressBinding
import com.example.smartlab.databinding.FragmentOrderBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n")
class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrderBinding
    private val calendar = Calendar.getInstance()

    private val locationPermissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
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
                    val myHour = if (hourOfDay >= 10) hourOfDay else "0$hourOfDay"
                    val myMinute = if (minute >= 10) minute else "0$minute"
                    binding.tvDateTime.text = " ${binding.tvDateTime.text} $myHour:$myMinute"
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
        isLocationPermissionGranted()
        Locale.setDefault(Locale("ru"))
        setListeners()
    }

    private fun setListeners() {
        binding.ivBtnBack.setOnClickListener { findNavController().popBackStack() }
        binding.tvAddress.setOnClickListener {
            showSelectAddressBottomSheetDialog()
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

    private fun showSelectAddressBottomSheetDialog() {
        val selectAddressDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheet)
        val selectAddressDialogBinding = BottomSheetAddressBinding.inflate(layoutInflater)
        selectAddressDialog.setContentView(selectAddressDialogBinding.root)
        selectAddressDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        selectAddressDialog.show()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            locationPermissionRequestLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            true
        }
    }
}