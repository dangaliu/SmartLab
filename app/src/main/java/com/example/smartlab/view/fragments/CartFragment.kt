package com.example.smartlab.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlab.databinding.FragmentCartBinding
import com.example.smartlab.model.dto.CatalogItem
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
        binding.ivClearAll.setOnClickListener {
            viewModel.clearAll()
        }
    }

    private fun setObservers() {
        viewModel.items.observe(viewLifecycleOwner) { items ->
            viewModel.cartItems = items.filter { it.isInCard }
            viewModel.cartItems.forEach {
                if (it.patientCount == 0) it.patientCount = 1
            }
            updateTotalPrice(viewModel.cartItems)
            cartAdapter.updateItems(viewModel.cartItems)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalPrice(cartItems: List<CatalogItem>) {
        var totalPrice = 0
        cartItems.forEach {
            totalPrice += it.price.toInt() * it.patientCount
        }
        binding.tvTotalPrice.text = "$totalPrice ₽"
    }

    private fun initCartRecyclerView() {
        cartAdapter = CartAdapter(
            requireContext(), listOf(),
            onMinusClickListener = { viewModel.onMinusClick(it) },
            onPlusClickListener = { viewModel.onPlusClick(it) },
            onDeleteClickListener = { viewModel.deleteFromCart(it) }
        )
        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }


}