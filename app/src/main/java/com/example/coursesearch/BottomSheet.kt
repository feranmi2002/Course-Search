package com.example.coursesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.couresearch.viewmodels.CourseSearchViewModelFactory
import com.example.couresearch.viewmodels.SearchViewModel
import com.example.coursesearch.databinding.CourseBinding
import com.example.coursesearch.retrofit.ApiHelper
import com.example.coursesearch.retrofit.ServiceBuilder
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet(val id: Int?) : BottomSheetDialogFragment() {
    private var _binding:CourseBinding? = null
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
            binding.info.visibility = View.INVISIBLE
            binding.progress.visibility = View.VISIBLE

        }
        binding.recycler.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.courseID.value = id
        viewModel.reviews.observe(viewLifecycleOwner, Observer {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
            if (adapter.itemCount > 0){
                binding.progress.visibility = View.INVISIBLE
            }else{
                binding.info.visibility = View.VISIBLE
                binding.retry.visibility = View.VISIBLE
            }

        })


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null
        viewModel.reviews.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }
    companion object{
        const val TAG = "ModalBottomSheet"
    }
}