package com.example.coursesearch.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.couresearch.viewmodels.CourseSearchViewModelFactory
import com.example.couresearch.viewmodels.SearchViewModel
import com.example.coursesearch.adapters.CourseReviewAdapter
import com.example.coursesearch.adapters.CourseSearchLoadStateAdapter
import com.example.coursesearch.databinding.CourseBinding
import com.example.coursesearch.retrofit.ApiHelper
import com.example.coursesearch.retrofit.ServiceBuilder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BottomSheet(val id: Int?) : BottomSheetDialogFragment() {
    private var _binding: CourseBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: CourseReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(
            this,
            CourseSearchViewModelFactory((ApiHelper(ServiceBuilder.apiService)))
        ).get(SearchViewModel::class.java)
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CourseReviewAdapter()
        binding.retry.setOnClickListener {
            viewModel.courseID.value = id
        }
        binding.recycler.adapter = adapter.withLoadStateHeaderAndFooter(
            header = CourseSearchLoadStateAdapter { adapter.retry() },
            footer = CourseSearchLoadStateAdapter { adapter.retry() }
        )
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.courseID.value = id
        viewModel.reviews.observe(viewLifecycleOwner, Observer {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        })
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                //show loading spinner during initial load or refresh
                binding.progress.isVisible =
                    loadState.refresh is LoadState.Loading
                // show error info
                binding.info.isVisible =
                    loadState.refresh is LoadState.Error && adapter.itemCount == 0
                binding.retry.isVisible =
                    loadState.refresh is LoadState.Error && adapter.itemCount == 0
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        viewModel.reviews.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}