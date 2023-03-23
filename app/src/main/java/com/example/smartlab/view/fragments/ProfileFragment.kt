package com.example.smartlab.view.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
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
            .apply { tvSkip.visibility = View.GONE }
        return createProfileBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextWatchersToEditTextInLayout()
        viewModel.getPatientCard()
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        if (editProfileBinding != null) {
            editProfileBinding!!.etGender.setOnClickListener {
                showGenderPopUpMenu(editProfileBinding!!.etGender)
            }
            editProfileBinding!!.btnSavePatientCard.setOnClickListener {
                with(editProfileBinding!!) {
                    val firstname = etName.text.toString()
                    val middlename = etMiddleName.text.toString()
                    val lastname = etSurname.text.toString()
                    val bith = etDateOfBirth.text.toString()
                    val gender = etGender.text.toString()
                    val image = ""
                    viewModel.updateProfile(
                        CreateProfileRequest(firstname, lastname, middlename, bith, gender, image)
                    )
                    viewModel.savePatientCard(
                        getPatientCardData(firstname, lastname, middlename, bith, gender, image)
                    )
                }
            }
        } else {
            createProfileBinding!!.etGender.setOnClickListener {
                showGenderPopUpMenu(createProfileBinding!!.etGender)
            }
            createProfileBinding!!.btnCreatePatientCard.setOnClickListener {
                with(createProfileBinding!!) {
                    val firstname = etName.text.toString()
                    val middlename = etMiddleName.text.toString()
                    val lastname = etSurname.text.toString()
                    val bith = etDateOfBirth.text.toString()
                    val gender = etGender.text.toString()
                    val image = ""

                    viewModel.createProfile(
                        getPatientCardData(firstname, lastname, middlename, bith, gender, image)
                    )
                    viewModel.savePatientCard(
                        getPatientCardData(firstname, lastname, middlename, bith, gender, image)
                    )
                }
            }
        }
    }

    private fun getPatientCardData(
        firstname: String,
        lastname: String,
        middlename: String,
        bith: String,
        gender: String,
        image: String
    ): CreateProfileRequest {
        return CreateProfileRequest(
            firstname = firstname,
            lastname = lastname,
            middlename = middlename,
            bith = bith,
            pol = gender,
            image = image
        )
    }

    private fun showGenderPopUpMenu(etGender: TextView) {
        val popUpMenu = PopupMenu(requireContext(), etGender)
        popUpMenu.inflate(R.menu.gender_popup_menu)
        popUpMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.male, R.id.female -> {
                    etGender.text = it.title
                    checkAllEditText()
                    true
                }
                else -> false
            }
        }
        popUpMenu.show()
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
                                editProfileBinding!!.btnSavePatientCard.backgroundTintList =
                                    ColorStateList.valueOf(resources.getColor(R.color.accent, null))
                                editProfileBinding!!.btnSavePatientCard.isEnabled = true
                            } else {
                                editProfileBinding!!.btnSavePatientCard.backgroundTintList =
                                    ColorStateList.valueOf(
                                        resources.getColor(R.color.accent_inactive, null)
                                    )
                                editProfileBinding!!.btnSavePatientCard.isEnabled = false
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