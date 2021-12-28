package com.example.coursesearch.fragments

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.couresearch.viewmodels.CourseSearchViewModelFactory
import com.example.couresearch.viewmodels.LibraryViewModel
import com.example.coursesearch.MainActivity
import com.example.coursesearch.R
import com.example.coursesearch.adapters.LibraryAdapter
import com.example.coursesearch.databinding.DescriptionBinding
import com.example.coursesearch.databinding.FragmentLibraryBinding
import com.example.coursesearch.models.Course
import com.example.coursesearch.retrofit.ApiHelper
import com.example.coursesearch.retrofit.ServiceBuilder
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class LibraryFragment : Fragment() {
    private var DATA_AVAILABLE: Boolean = false
    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LibraryViewModel
    private lateinit var adapter: LibraryAdapter
    private var data: List<Course> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.progressCircular.visibility = View.VISIBLE
        binding.info.visibility = View.INVISIBLE
        val parentActivity = requireActivity() as MainActivity
        viewModel.database = parentActivity.getDatabase()
        adapter = LibraryAdapter(data) { click, course ->
            when {
                click == "See reviews" -> handleReviews(course.id, course.title!!)
                click == "Library" -> handleLibrary(course)
                click == "Link" -> handleLink(course.url!!)
                click == "Description" -> handleSeeDescriptionInFull(course)
            }

        }
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.getLibrary()
            .observe(viewLifecycleOwner, Observer { data ->
                if (data.isNotEmpty()) {
                    binding.info.visibility = View.INVISIBLE
                    DATA_AVAILABLE = true
                    binding.progressCircular.visibility = View.INVISIBLE
                    adapter.submitData(data)
                    adapter.notifyDataSetChanged()
                } else {
                    binding.info.text = "No Courses Added Yet"
                    binding.info.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.INVISIBLE
                }

            })


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }


    private fun handleLibrary(course: Course) {
        viewModel.deleteCourse(course)

    }

    private fun handleLink(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.udemy.com${url}")))
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
}