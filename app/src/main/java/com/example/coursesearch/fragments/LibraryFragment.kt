package com.example.coursesearch.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.couresearch.viewmodels.CourseSearchViewModelFactory
import com.example.couresearch.viewmodels.LibraryViewModel
import com.example.couresearch.viewmodels.SearchViewModel
import com.example.coursesearch.*
import com.example.coursesearch.databinding.FragmentLibraryBinding
import com.example.coursesearch.models.Course
import com.example.coursesearch.retrofit.ApiHelper
import com.example.coursesearch.retrofit.ServiceBuilder
import kotlinx.coroutines.launch


class LibraryFragment : Fragment() {
    private var DATA_AVAILABLE: Boolean = false
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LibraryViewModel
    private lateinit var adapter: LibraryAdapter
    private var data: List<Course> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(
            this,
            CourseSearchViewModelFactory((ApiHelper(ServiceBuilder.apiService)))
        ).get(LibraryViewModel::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_all -> handleDeleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu[0].isEnabled = DATA_AVAILABLE
        super.onPrepareOptionsMenu(menu)
    }

    private fun handleDeleteAll() {
        val parentActivity = requireActivity() as MainActivity
        viewModel.deleteAll(parentActivity.getDatabase())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.library, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.progressCircular.visibility = View.VISIBLE
        binding.info.visibility = View.INVISIBLE
        lifecycleScope.launch {
            val parentActivity = requireActivity() as MainActivity
            viewModel.getLibrary(parentActivity.getDatabase())
            adapter = LibraryAdapter(viewModel.getLibrary(parentActivity.getDatabase()).value){ click, course ->
                when {
                    click.equals("See reviews") -> handleReviews(course.id, course.title!!)
                    click.equals("Library") -> handleLibrary(course)
                    click.equals("Link") -> handleLink(course.url!!)
                }

            }
            binding.recycler.adapter = adapter
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())

        }
        viewModel.data.observe(viewLifecycleOwner, Observer {data ->
            if (data.size > 0){
                DATA_AVAILABLE = true
                binding.progressCircular.visibility = View.INVISIBLE
                adapter.submitData(data)
                adapter.notifyDataSetChanged()
            }else{
                binding.info.text = "No Courses Added Yet"
                binding.info.visibility = View.VISIBLE
            }

        })
        val parentActivity = requireActivity() as MainActivity
        viewModel.getLibrary(parentActivity.getDatabase())
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }



    private fun handleLibrary(course:Course) {
        val parentActivity = requireActivity() as MainActivity
        parentActivity.getDatabase().delete(course)


    }

    private fun handleLink(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com${url}")))
    }

    private fun handleReviews(id: Int?, courseTitle: String) {
        val modalBottomSheet = BottomSheet(id)
        modalBottomSheet.show(requireActivity().supportFragmentManager, BottomSheet.TAG)
    }
}