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
import com.example.smartlab.databinding.FragmentPatientCardBinding
import com.example.smartlab.databinding.FragmentProfileBinding
import com.example.smartlab.model.api.requestModels.CreateProfileRequest
import com.example.smartlab.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private var editProfileBinding: FragmentProfileBinding? = null
    private var createProfileBinding: FragmentPatientCardBinding? = null
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPatientCard()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.patientCard.value?.let {
            if (it.firstname != null) {
                editProfileBinding = FragmentProfileBinding.inflate(inflater, container, false)
                viewModel.isEditMode = true
                return editProfileBinding!!.root
            }
        }
        createProfileBinding = FragmentPatientCardBinding.inflate(inflater, container, false)
        return createProfileBinding!!.root
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

    private fun setPatientCardData(patientCard: CreateProfileRequest) {
        val binding = if (viewModel.isEditMode) editProfileBinding else createProfileBinding
        if (binding is FragmentProfileBinding) {
            with(editProfileBinding!!) {
                etName.setText(patientCard.firstname)
                etMiddleName.setText(patientCard.middlename)
                etSurname.setText(patientCard.lastname)
                etDateOfBirth.setText(patientCard.bith)
                etGender.text = patientCard.pol
            }
        }

    }

    private fun setTextWatchersToEditTextInLayout() {
        if (editProfileBinding != null) {
            editProfileBinding!!.mainContainer.iterator().forEach {
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
                                editProfileBinding!!.btnCreatePatientCard.backgroundTintList =
                                    ColorStateList.valueOf(resources.getColor(R.color.accent, null))
                                editProfileBinding!!.btnCreatePatientCard.isEnabled = true
                            } else {
                                editProfileBinding!!.btnCreatePatientCard.backgroundTintList =
                                    ColorStateList.valueOf(
                                        resources.getColor(R.color.accent_inactive, null)
                                    )
                                editProfileBinding!!.btnCreatePatientCard.isEnabled = false
                            }
                        }
                    })
                }
            }
        } else {
            createProfileBinding!!.mainContainer.iterator().forEach {
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
                                createProfileBinding!!.btnCreatePatientCard.backgroundTintList =
                                    ColorStateList.valueOf(resources.getColor(R.color.accent, null))
                                createProfileBinding!!.btnCreatePatientCard.isEnabled = true
                            } else {
                                createProfileBinding!!.btnCreatePatientCard.backgroundTintList =
                                    ColorStateList.valueOf(
                                        resources.getColor(R.color.accent_inactive, null)
                                    )
                                createProfileBinding!!.btnCreatePatientCard.isEnabled = false
                            }
                        }
                    })
                }
            }
        }
    }

    private fun checkAllEditText(): Boolean {
        val binding = if (viewModel.isEditMode) editProfileBinding else createProfileBinding
        if (binding is FragmentProfileBinding) {
            return with(binding) {
                etName.text.isNotBlank() && etMiddleName.text.isNotBlank() &&
                        etSurname.text.isNotBlank() && etDateOfBirth.text.isNotBlank() &&
                        etGender.text.isNotBlank()
            }
        } else {
            return with(createProfileBinding!!) {
                etName.text.isNotBlank() && etMiddleName.text.isNotBlank() &&
                        etSurname.text.isNotBlank() && etDateOfBirth.text.isNotBlank() &&
                        etGender.text.isNotBlank()
            }
        }

    }
}