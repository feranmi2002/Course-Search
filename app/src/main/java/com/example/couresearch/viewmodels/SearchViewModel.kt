package com.example.couresearch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.coursesearch.data.CourseReviewPagingSource
import com.example.coursesearch.data.CourseSearchPagingSource
import com.example.coursesearch.models.Course
import com.example.coursesearch.retrofit.ApiHelper
import com.example.coursesearch.room.CourseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(val apiHelper: ApiHelper) : ViewModel() {
    val courseID: MutableLiveData<Int?> = MutableLiveData()
    private val queryConstraint: MutableLiveData<String> = MutableLiveData(null)
    val query: MutableLiveData<String> = MutableLiveData()
    var category: String? = null
    var price: String? = null
    var instructional_level: String? = null
    var ratings: String? = null
    var duration: String? = null
    var ordering: String? = null

    val reviews = courseID.switchMap {
        reviews(it!!).cachedIn(viewModelScope)
    }

    fun insertCourse(course: Course, database: CourseDao) {
        viewModelScope.launch(Dispatchers.IO) {
            database.insertAll(course)
        }
    }

    val result = query.switchMap {
        search(query(it)).cachedIn(viewModelScope)
    }

    private fun query(query: String): HashMap<String, String> {
        val hashMap = HashMap<String, String>()
        hashMap["search"] = query
        if (category!=null) {
            hashMap["category"] = category!!
        }
        if (price!=null) {
            hashMap["price"] = price!!
        }
        if (instructional_level!=null) {
            hashMap["instructional_level"] = instructional_level!!
        }
        if (duration!=null) {
            hashMap["duration"] = duration!!
        }
        if (ordering!=null) {
            hashMap["ordering"] = ordering!!
        }
        return hashMap
    }

    private fun reviews(course_id: Int) = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 30,
            enablePlaceholders = true
        ), pagingSourceFactory = {
            CourseReviewPagingSource(apiHelper, course_id)
        }, initialKey = 1
    ).liveData


    private fun search(query: HashMap<String, String>) = Pager(
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