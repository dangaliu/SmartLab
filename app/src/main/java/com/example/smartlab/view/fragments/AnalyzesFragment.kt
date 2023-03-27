package com.example.smartlab.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlab.R
import com.example.smartlab.databinding.BottomSheetDialogAnalyzItemBinding
import com.example.smartlab.databinding.ChipItemBinding
import com.example.smartlab.databinding.FragmentAnalyzesBinding
import com.example.smartlab.model.dto.CatalogItem
import com.example.smartlab.view.adapters.CatalogAdapter
import com.example.smartlab.view.adapters.NewsAdapter
import com.example.smartlab.view.adapters.SearchAdapter
import com.example.smartlab.viewmodel.AnalyzesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AnalyzesFragment : Fragment() {

    private lateinit var binding: FragmentAnalyzesBinding
    private val viewModel: AnalyzesViewModel by viewModels()

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var catalogAdapter: CatalogAdapter
    private lateinit var searchAdapter: SearchAdapter

    private val TAG = this::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnalyzesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        initSwipeRefreshLayout()
        initNewsRecyclerView()
        initCatalogRecyclerView()
        initSearchRecyclerView()
        viewModel.getNews()
        viewModel.getCatalog()
        setObservers()
    }

    private fun setListeners() {
        binding.etSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tvCancel.visibility = View.VISIBLE
                binding.mainContainer.visibility = View.GONE
                binding.searchResultsContainer.visibility = View.VISIBLE
                binding.etSearch.addTextChangedListener(object : TextWatcher {
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
                        if (s.toString().length >= 3) {
                            viewModel.catalog.value?.let {
                                binding.searchResultsContainer.visibility = View.VISIBLE
                                val searchItems = it.filter { catalogItem ->
                                    catalogItem.name.contains(s.toString())
                                }
                                searchAdapter.updateItems(searchItems)
                            }
                        }
                    }

                })
            } else {
                binding.tvCancel.visibility = View.GONE
                binding.mainContainer.visibility = View.VISIBLE
                binding.searchResultsContainer.visibility = View.GONE
            }
        }
        binding.tvCancel.setOnClickListener {
            binding.etSearch.clearFocus()
        }
    }

    private fun setObservers() {
        viewModel.news.observe(viewLifecycleOwner) {
            newsAdapter.updateItems(it)
        }
        viewModel.catalog.observe(viewLifecycleOwner) {
            viewModel.dbCatalog.value?.let { catalogDb ->
                if (catalogDb.isEmpty()) {
                    viewModel.fillDatabase(it)
                    catalogAdapter.updateItems(it)
                }
            }

        }
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            if (binding.chipGroup.isEmpty()) {
                categories.forEachIndexed { index, category ->
                    val chip =
                        ChipItemBinding.inflate(layoutInflater).rootChip.apply { text = category }
                    binding.chipGroup.addView(chip.apply {
                        id = index
                    })
                }
            }
        }
        viewModel.dbCatalog.observe(viewLifecycleOwner) {
            Log.d(TAG, "setObservers: dbCatalog $it")
            catalogAdapter.updateItems(it)
        }
    }

    private fun initNewsRecyclerView() {
        newsAdapter = NewsAdapter(requireContext(), listOf())
        binding.rvNews.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = newsAdapter
        }
    }

    private fun showAnalyzItemBottomSheetDialog(analyzItem: CatalogItem) {
        val analyzItemBinding = BottomSheetDialogAnalyzItemBinding.inflate(layoutInflater)
        val analyzItemDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheet)
        analyzItemBinding.apply {
            tvTitle.text = analyzItem.name
            tvDescription.text = analyzItem.description
            tvPreparation.text = analyzItem.preparation
            tvTimerResult.text = analyzItem.time_result
            tvBio.text = analyzItem.bio
            btnAdd.text = "${analyzItem.price} ₽"
            ivClose.setOnClickListener { analyzItemDialog.cancel() }
        }
        analyzItemDialog.setContentView(analyzItemBinding.root)
        analyzItemDialog.show()
    }

    private fun initSearchRecyclerView() {
        searchAdapter = SearchAdapter(requireContext(), listOf())
        binding.rvSearchResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun initCatalogRecyclerView() {
        catalogAdapter = CatalogAdapter(
            requireContext(), listOf(),
            onCardClickListener = {
                showAnalyzItemBottomSheetDialog(it)
            },
            onAddButtonClickListener = {
                viewModel.updateCatalogItem(it.copy(isInCard = !it.isInCard))
            }
        )
        binding.rvCatalog.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = catalogAdapter
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.root.setOnRefreshListener {
            lifecycleScope.launch {
                delay(1000)
                viewModel.getNews()
                viewModel.getCatalog()
                binding.root.isRefreshing = false
            }
        }
    }
}