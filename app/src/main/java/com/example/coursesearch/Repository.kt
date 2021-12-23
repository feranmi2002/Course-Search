package com.example.coursesearch

import com.example.coursesearch.retrofit.ApiHelper

object Repository {
    suspend fun getCourses(apiHelper:ApiHelper, query: HashMap<String, String?>, page: Int) = apiHelper.getCourses(query, page)
    suspend fun getCoursesReview(apiHelper: ApiHelper, course_id: Int, key: Int) = apiHelper.getCourseReviews(key,course_id )
}