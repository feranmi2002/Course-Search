package com.example.couresearch.viewmodels

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.coursesearch.CourseDao
import com.example.coursesearch.CourseReviewPagingSource
import com.example.coursesearch.CourseSearchPagingSource
import com.example.coursesearch.models.Course
import com.example.coursesearch.retrofit.ApiHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(val apiHelper: ApiHelper) : ViewModel() {
    val courseID: MutableLiveData<Int> = MutableLiveData()
    private val queryConstraint: MutableLiveData<String> = MutableLiveData()
    val query: MutableLiveData<String> = MutableLiveData()
    var category: String? = null
    var price: String? = null
    var instructional_level: String? = null
    var ratings: String? = null
    var duration: String? = null
    var ordering: String? = null

    val reviews = courseID.switchMap {
        reviews(it).cachedIn(viewModelScope)
    }

    fun insertCourse(course: Course, database: CourseDao){
        viewModelScope.launch(Dispatchers.IO) {
            database.insertAll(course)
        }

    }


//    var result:MutableLiveData<Resource<CourseWrapper>> = MutableLiveData()

    val result = query.switchMap {
        val hashMap = HashMap<String, String?>()
        hashMap.put("search", query.value)
        hashMap.put("category", category)
        hashMap.put("price", price)
        hashMap.put("instructional_level", instructional_level)
        hashMap.put("duration", duration)
        hashMap.put("ordering", ordering)
        search(hashMap).cachedIn(viewModelScope)
    }

    private fun reviews(course_id:Int) = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 30,
            enablePlaceholders = true
        ), pagingSourceFactory = {
            CourseReviewPagingSource(apiHelper, course_id)
        }, initialKey = 1
    ).liveData


    private fun search(query: HashMap<String, String?>) = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 30,
            enablePlaceholders = true
        ), pagingSourceFactory = {
            CourseSearchPagingSource(apiHelper, query)
        }, initialKey = 1
    ).liveData

    fun submitQueryCategory(text: String) {
        queryConstraint.value = text

    }

    fun submitQuery(query: String) {
        this.query.value = query
    }

}