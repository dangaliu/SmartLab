package com.example.smartlab.view.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.example.smartlab.R
import com.example.smartlab.databinding.FragmentPatientCardBinding

class PatientCardFragment : Fragment() {

    private lateinit var binding: FragmentPatientCardBinding

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
    }

    private fun setListeners() {
        binding.etGender.setOnClickListener {
            showGenderPopUpMenu()
        }
    }

    private fun showGenderPopUpMenu() {
        val popUpMenu = PopupMenu(requireContext(), binding.etGender)
        popUpMenu.inflate(R.menu.gender_popup_menu)
        popUpMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.male, R.id.female -> {
                    binding.etGender.setText(it.title)
                    true
                }
                else -> false
            }
        }
        popUpMenu.show()
    }

    private fun setTextWatchersToEditTextInLayout() {
        binding.mainContainer.iterator().forEach {
            if (it is EditText) {
                (it as EditText).addTextChangedListener(object : TextWatcher {
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
                            binding.btnCreatePatientCard.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.accent, null))
                            binding.btnCreatePatientCard.isEnabled = true
                        } else {
                            binding.btnCreatePatientCard.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.accent_inactive, null))
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