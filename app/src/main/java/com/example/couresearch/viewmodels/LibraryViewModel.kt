package com.example.couresearch.viewmodels

import androidx.lifecycle.*
import com.example.coursesearch.room.CourseDao
import com.example.coursesearch.models.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryViewModel() : ViewModel() {
    lateinit var database: CourseDao
    var data: LiveData<Course> = MutableLiveData()
    fun getLibrary() = database.getAll()

    fun deleteCourse(course: Course) {
        viewModelScope.launch(Dispatchers.IO){
            database.delete(course)
        }
    }
}

