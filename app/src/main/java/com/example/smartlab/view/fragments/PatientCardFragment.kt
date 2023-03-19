package com.example.smartlab.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
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
}