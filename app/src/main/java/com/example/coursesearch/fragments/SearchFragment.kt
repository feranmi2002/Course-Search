package com.example.coursesearch.fragments

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.couresearch.viewmodels.CourseSearchViewModelFactory
import com.example.couresearch.viewmodels.SearchViewModel
import com.example.coursesearch.MainActivity
import com.example.coursesearch.R
import com.example.coursesearch.adapters.CourseRecyclerAdapter
import com.example.coursesearch.adapters.CourseSearchLoadStateAdapter
import com.example.coursesearch.databinding.DescriptionBinding
import com.example.coursesearch.databinding.FilterBinding
import com.example.coursesearch.databinding.SearchBinding
import com.example.coursesearch.models.Course
import com.example.coursesearch.retrofit.ApiHelper
import com.example.coursesearch.retrofit.ServiceBuilder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class SearchFragment : Fragment() {

    private var query: String? = null
    private var _binding: SearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: CourseRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(
            this,
            CourseSearchViewModelFactory((ApiHelper(ServiceBuilder.apiService)))
        ).get(SearchViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = SearchBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpAdapter()
        setUpRetryButton()
        setupObserver()
        setUpLoadState()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchView = menu.get(0).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                handleQuerySubmit(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // do nothing
                return false
            }
        })
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_filter -> {
                viewModel.ordering = null
                viewModel.instructional_level = null
                viewModel.price = null
                viewModel.duration = null
                viewModel.category = null
                Snackbar.make(binding.root, "Filter has been cleared", Snackbar.LENGTH_SHORT).show()
            }
            R.id.filter -> handleFilter()
            R.id.library -> findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToLibraryFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleFilter() {
        val binding = FilterBinding.inflate(LayoutInflater.from(requireContext()))
        binding.apply {
            var items: Array<String>?
            var arrayAdapter: ArrayAdapter<String>?
            for (index in 1..6) when (index) {
                1 -> {
                    items = requireContext().resources.getStringArray(R.array.price)
                    arrayAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        items as Array<out String>
                    )
                    (priceLayout.editText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)
                }
                2 -> {
                    items = requireContext().resources.getStringArray(R.array.level)
                    arrayAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        items as Array<out String>
                    )
                    (levelLayout.editText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)
                }
                3 -> {
                    items = requireContext().resources.getStringArray(R.array.category)
                    arrayAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        items as Array<out String>
                    )
                    (categoryLayout.editText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)
                }
                4 -> {
                    items = requireContext().resources.getStringArray(R.array.duration)
                    arrayAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        items as Array<out String>
                    )
                    (durationLayout.editText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)
                }
                5 -> {
                    items = requireContext().resources.getStringArray(R.array.ordering)
                    arrayAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        items as Array<out String>
                    )
                    (orderingLayout.editText as? AutoCompleteTextView)?.setAdapter(arrayAdapter)
                }
            }
        }
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setTitle("Filter your Search")
            .setIcon(R.drawable.ic_baseline_filter_list_24)
            .setPositiveButton("Done", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    viewModel.category = binding.categoryLayout.editText?.text.toString()
                    if (binding.durationLayout.editText?.text.toString().equals("Extra Long")) {
                        viewModel.duration = "extraLong"
                    } else if (binding.durationLayout.editText?.text.toString().equals("Short")
                        || binding.durationLayout.editText?.text.toString().equals("Medium")
                        || binding.durationLayout.editText?.text.toString().equals("Long")
                    ) viewModel.duration =
                        binding.durationLayout.editText?.text.toString().lowercase(
                            Locale.ENGLISH
                        )

                    viewModel.price = "price-${
                        binding.priceLayout.editText?.text.toString().lowercase(
                            Locale.ENGLISH
                        )
                    }"
                    viewModel.instructional_level =
                        binding.levelLayout.editText?.text.toString().lowercase(Locale.ENGLISH)
                    viewModel.ordering =
                        binding.orderingLayout.editText?.text.toString().lowercase(Locale.ENGLISH)
                }
            })
            .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    // do nothing
                }
            }).create()
        dialog.show()
    }


    private fun handleQuerySubmit(query: String?) = if (query?.isEmpty() == true) Snackbar.make(
        binding.root,
        "Enter Search text",
        Snackbar.LENGTH_SHORT
    ).show()
    else {
        this.query = query
        viewModel.query.value = query
    }

    private fun setupObserver() {
        viewModel.result.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitData(viewLifecycleOwner.lifecycle, result)
        })
    }
    private fun setUpRetryButton(){
        binding.retry.setOnClickListener {
            viewModel.query.value = query
        }
    }

    private fun setUpLoadState() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
                // show empty list
                binding.info.isVisible = isListEmpty

                //show loading spinner during initial load or refresh
                binding.progressCircular.isVisible =
                    loadState.refresh is LoadState.Loading
                // show error info
                binding.error.isVisible =
                    loadState.refresh is LoadState.Error && adapter.itemCount == 0
                binding.retry.isVisible =
                    loadState.refresh is LoadState.Error && adapter.itemCount == 0
            }
        }
    }


    private fun setUpAdapter() {
        adapter = CourseRecyclerAdapter() { click, course ->
            when {
                click == "See reviews" -> handleReviews(course.id, course.title!!)
                click == "Library" -> handleLibrary(course)
                click == "Link" -> handleLink(course.url!!)
                click == "Description" -> handleSeeDescriptionInFull(course)
            }
        }
        binding.apply {
            recycler.adapter = adapter.withLoadStateHeaderAndFooter(
                header = CourseSearchLoadStateAdapter { adapter.retry() },
                footer = CourseSearchLoadStateAdapter { adapter.retry() }
            )
            recycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


    private fun handleLink(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com${url}")))
    }

    private fun handleLibrary(course: Course) {
        val parentActivity = requireActivity() as MainActivity
        viewModel.insertCourse(course, parentActivity.getDatabase())
        Snackbar.make(
            binding.root,
            "${course.title} has been added to your library",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun handleReviews(id: Int?, courseTitle: String) {
        val modalBottomSheet = BottomSheet(id)
        modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheet.TAG)
    }

    private fun handleSeeDescriptionInFull(course: Course) {
        val binding = DescriptionBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        binding.description.text = course.headline
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(course.title)
            .setCancelable(true)
            .setView(binding.root)
            .setPositiveButton("Okay", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    // do nothing, dialog automatically dismisses
                }
            }).create()
        dialog.show()
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}