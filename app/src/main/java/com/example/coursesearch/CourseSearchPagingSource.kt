package com.example.coursesearch

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coursesearch.models.Course
import com.example.coursesearch.retrofit.ApiHelper

class CourseSearchPagingSource(val apiHelper: ApiHelper, val query: HashMap<String, String?>) : PagingSource<Int, Course>() {
    override fun getRefreshKey(state: PagingState<Int, Course>): Int? {
        return null
    }

    override val keyReuseSupported: Boolean
        get() = false

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Course> {
        return try {
            val response = Repository.getCourses(apiHelper, query, params.key!!)
            val next = if (params.key!=null) params.key?.plus(1)
            else null
            LoadResult.Page(
                data = response.results,
                nextKey = next,
                prevKey = if (params.key == 1){
                    null
                }else{
                    params.key?.minus(1)
                }
            )
        }catch (exception:Exception){
            LoadResult.Error(exception)
        }
    }
}