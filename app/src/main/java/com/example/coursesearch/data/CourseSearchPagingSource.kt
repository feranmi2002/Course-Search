package com.example.coursesearch.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coursesearch.models.Course
import com.example.coursesearch.retrofit.ApiHelper

class CourseSearchPagingSource(val apiHelper: ApiHelper, val query: HashMap<String, String>) :
    PagingSource<Int, Course>() {
    override fun getRefreshKey(state: PagingState<Int, Course>): Int? {
        return null
    }

    override val keyReuseSupported: Boolean
        get() = false

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Course> {
        return try {
            val response = Repository.getCourses(apiHelper, query, params.key!!)
            // this loop is to get the authors of each course
            val results = mutableListOf<Course>()
            for (course in response.results){
                course.visible_instructors?.onEach { instructor ->
                    course.authors = course.authors + instructor.name
                }
                results.add(course)
            }
            val next = if (params.key != null) params.key?.plus(1)
            else null
            LoadResult.Page(
                data = results,
                nextKey = next,
                prevKey = if (params.key == 1) {
                    null
                } else {
                    params.key?.minus(1)
                }
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}