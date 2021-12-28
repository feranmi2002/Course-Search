package com.example.coursesearch.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coursesearch.models.CourseReview
import com.example.coursesearch.retrofit.ApiHelper

class CourseReviewPagingSource(val apiHelper: ApiHelper, val course_id:Int) :
    PagingSource<Int, CourseReview>() {
    override fun getRefreshKey(state: PagingState<Int, CourseReview>): Int? {
        return null
    }

    override val keyReuseSupported: Boolean
        get() = false

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CourseReview> {
        return try {
            val response = Repository.getCoursesReview(apiHelper, course_id, params.key!!)
            LoadResult.Page(
                data = response.results,
                nextKey = params.key?.plus(1),
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