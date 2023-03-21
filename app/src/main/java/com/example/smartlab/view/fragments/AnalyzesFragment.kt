package com.example.smartlab.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlab.databinding.FragmentAnalyzesBinding
import com.example.smartlab.view.adapters.NewsAdapter
import com.example.smartlab.viewmodel.AnalyzesViewModel

class AnalyzesFragment : Fragment() {

    private lateinit var binding: FragmentAnalyzesBinding
    private val viewModel: AnalyzesViewModel by viewModels()

    private lateinit var newsAdapter: NewsAdapter

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
        initNewsRecyclerView()
        viewModel.getNews()
        setObservers()
    }

    private fun setObservers() {
        viewModel.news.observe(viewLifecycleOwner) {
            newsAdapter.updateItems(it)
        }
    }

    private fun initNewsRecyclerView() {
        newsAdapter = NewsAdapter(requireContext(), listOf())
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = newsAdapter
        }
    }
}