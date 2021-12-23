package com.example.couresearch.viewmodels

import androidx.lifecycle.*
import com.example.coursesearch.CourseDao
import com.example.coursesearch.CourseDatabase
import com.example.coursesearch.MainActivity
import com.example.coursesearch.Repository
import com.example.coursesearch.models.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class LibraryViewModel() : ViewModel() {
    var data: LiveData<List<Course>> = MutableLiveData()
    fun getLibrary(courseDao: CourseDao) = courseDao.getAll()


    fun deleteAll(database: CourseDao) {
        viewModelScope.launch(Dispatchers.IO) {
            database.deleteAll(data.value!!)
        }
    }
}

