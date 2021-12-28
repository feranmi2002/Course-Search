package com.example.couresearch.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coursesearch.retrofit.ApiHelper

class CourseSearchViewModelFactory(private val apiHelper: ApiHelper): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java))
            return SearchViewModel(apiHelper ) as T
        if (modelClass.isAssignableFrom(LibraryViewModel::class.java))
            return LibraryViewModel() as T
        throw IllegalArgumentException("Unknown Class")
    }
}