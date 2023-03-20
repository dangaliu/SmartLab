package com.example.smartlab.view.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.smartlab.R
import com.example.smartlab.databinding.FragmentPatientCardBinding
import com.example.smartlab.model.api.requestModels.CreateProfileRequest
import com.example.smartlab.viewmodel.PatientCardViewModel

class PatientCardFragment : Fragment() {

    private lateinit var binding: FragmentPatientCardBinding
    private val viewModel: PatientCardViewModel by viewModels()

    private val TAG = this::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPatientCardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setTextWatchersToEditTextInLayout()
        setObservers()
    }

    private fun setListeners() {
        binding.etGender.setOnClickListener {
            showGenderPopUpMenu()
        }
        binding.btnCreatePatientCard.setOnClickListener {
            viewModel.createProfile(getPatientCardData())
        }
        binding.tvSkip.setOnClickListener {
            findNavController().navigate(R.id.action_patientCardFragment_to_mainFragment)
        }
    }

    private fun setObservers() {
        viewModel.createProfileStatus.observe(viewLifecycleOwner) {
            Log.d(TAG, "setObservers: create profile $it")
            if (it != null) {
                findNavController().navigate(R.id.action_patientCardFragment_to_mainFragment)
            }
        }
    }

    private fun showGenderPopUpMenu() {
        val popUpMenu = PopupMenu(requireContext(), binding.etGender)
        popUpMenu.inflate(R.menu.gender_popup_menu)
        popUpMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.male, R.id.female -> {
                    binding.etGender.text = it.title
                    checkAllEditText()
                    true
                }
                else -> false
            }
        }
        popUpMenu.show()
    }

    private fun getPatientCardData(): CreateProfileRequest {
        val firstname = binding.etName.text.toString()
        val lastname = binding.etSurname.text.toString()
        val middlename = binding.etMiddleName.text.toString()
        val bith = binding.etDateOfBirth.text.toString()
        val gender = binding.etGender.text.toString()
        val image = ""
        return CreateProfileRequest(
            firstname = firstname,
            lastname = lastname,
            middlename = middlename,
            bith = bith,
            pol = gender,
            image = image
        )
    }

    private fun setTextWatchersToEditTextInLayout() {
        binding.mainContainer.iterator().forEach {
            if (it is TextView) {
                it.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        if (checkAllEditText()) {
                            binding.btnCreatePatientCard.backgroundTintList =
                                ColorStateList.valueOf(resources.getColor(R.color.accent, null))
                            binding.btnCreatePatientCard.isEnabled = true
                        } else {
                            binding.btnCreatePatientCard.backgroundTintList =
                                ColorStateList.valueOf(
                                    resources.getColor(
                                        R.color.accent_inactive,
                                        null
                                    )
                                )
                            binding.btnCreatePatientCard.isEnabled = false
                        }
                    }

                })
            }
        }
    }

    private fun checkAllEditText(): Boolean {
        return with(binding) {
            etName.text.isNotBlank() && etMiddleName.text.isNotBlank() &&
                    etSurname.text.isNotBlank() && etDateOfBirth.text.isNotBlank() &&
                    etGender.text.isNotBlank()
        }
    }
}