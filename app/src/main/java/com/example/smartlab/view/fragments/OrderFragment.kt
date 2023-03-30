package com.example.smartlab.view.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.smartlab.R
import com.example.smartlab.databinding.BottomSheetAddressBinding
import com.example.smartlab.databinding.FragmentOrderBinding
import com.example.smartlab.viewmodel.OrderViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round
import kotlin.properties.Delegates

@SuppressLint("SetTextI18n")
class OrderFragment : Fragment() {

    private val REQUEST_CODE_SPEECH_INPUT = 100

    private lateinit var binding: FragmentOrderBinding
    private val calendar = Calendar.getInstance()

    private val viewModel: OrderViewModel by viewModels()
    private lateinit var locationManager: LocationManager
    private var hasGps by Delegates.notNull<Boolean>()

    private var speechStartTime by Delegates.notNull<Long>()
    private var speechEndTime by Delegates.notNull<Long>()

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Говорите сейчас!")
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Ошибка: " + e.message, Toast.LENGTH_LONG).show()
        }
    }

    // Обрабатываем результат голосового ввода
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SPEECH_INPUT -> {
                if (data != null) {
                    speechEndTime = System.currentTimeMillis()
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    binding.etComment.setText(result.toString().trim('[', ']'))
                    if (speechEndTime - speechStartTime > 20000) {
                        Toast.makeText(
                            requireContext(),
                            "Вы превысили 20 секунд",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }
            }
        }
    }

    private val gpsLocationListener: LocationListener =
        LocationListener { location ->
            viewModel.currentLocation.value = location
            selectAddressDialogBinding?.let {
                it.etLatitude.setText(round(location.latitude).toInt().toString())
                it.etLongitude.setText(round(location.longitude).toInt().toString())
                it.etHeight.setText(round(location.altitude).toInt().toString())
                viewModel.reverseGeocoding(lat = location.latitude, lon = location.longitude)
            }
            Log.d(TAG, "showSelectAddressBottomSheetDialog: ${viewModel.currentLocation.value}")
            Log.d(
                TAG,
                "showSelectAddressBottomSheetDialog: ${viewModel.currentLocation.value?.latitude}"
            )
            Log.d(
                TAG,
                "showSelectAddressBottomSheetDialog: ${viewModel.currentLocation.value?.longitude}"
            )
        }

    private var selectAddressDialogBinding: BottomSheetAddressBinding? = null

    private val locationPermissionRequestLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}
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
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        binding = FragmentOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: $hasGps")
        setObservers()
        if (hasGps) {
            if (isLocationPermissionGranted()) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    0f,
                    gpsLocationListener
                )
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Включите gps на смартфоне, а затем перезапустите приложение",
                Toast.LENGTH_LONG
            ).show()
        }
        if (!isLocationPermissionGranted()) {
            locationPermissionRequestLauncher.launch(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        Locale.setDefault(Locale("ru"))
        setListeners()
    }

    private fun setListeners() {
        binding.ivBtnBack.setOnClickListener { findNavController().popBackStack() }
        binding.tvAddress.setOnClickListener {
            showSelectAddressBottomSheetDialog()
        }
        binding.ivMic.setOnClickListener {
            startVoiceInput()
            speechStartTime = System.currentTimeMillis()
        }
    }

    private fun setObservers() {
        viewModel.reversedGeocoding.observe(viewLifecycleOwner) { reversedGeocoging ->
            selectAddressDialogBinding?.etAddress?.setText("${reversedGeocoging.features[0].properties.address.road} ${reversedGeocoging.features[0].properties.address.house_number}")
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
        selectAddressDialogBinding = BottomSheetAddressBinding.inflate(layoutInflater)
        selectAddressDialog.setContentView(selectAddressDialogBinding!!.root)
        selectAddressDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        selectAddressDialog.show()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}