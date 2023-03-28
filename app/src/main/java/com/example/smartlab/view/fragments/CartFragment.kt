package com.example.smartlab.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlab.databinding.FragmentCartBinding
import com.example.smartlab.view.adapters.CartAdapter
import com.example.smartlab.viewmodel.CartViewModel

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObservers()
        initCartRecyclerView()
    }

    private fun setListeners() {
        binding.ivBtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setObservers() {
        viewModel.items.observe(viewLifecycleOwner) { items ->
            val cartItems = items.filter { it.isInCard }
            cartAdapter.updateItems(cartItems)
        }
    }

    private fun initCartRecyclerView() {
        cartAdapter = CartAdapter(requireContext(), listOf())
        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }


}