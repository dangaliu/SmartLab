package com.example.smartlab.view.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.smartlab.R
import com.example.smartlab.databinding.FragmentProfileBinding
import com.example.smartlab.model.api.requestModels.CreateProfileRequest
import com.example.smartlab.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextWatchersToEditTextInLayout()
        viewModel.getPatientCard()
        setObservers()
    }

    private fun setObservers() {
        viewModel.patientCard.observe(viewLifecycleOwner) {
            it?.let { patientCard: CreateProfileRequest ->
                setPatientCardData(patientCard)
            }
        }
    }

    fun setPatientCardData(patientCard: CreateProfileRequest) {
        with(binding) {
            etName.setText(patientCard.firstname)
            etMiddleName.setText(patientCard.middlename)
            etSurname.setText(patientCard.lastname)
            etDateOfBirth.setText(patientCard.bith)
            etGender.text = patientCard.pol
        }
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
                                    resources.getColor(R.color.accent_inactive, null)
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