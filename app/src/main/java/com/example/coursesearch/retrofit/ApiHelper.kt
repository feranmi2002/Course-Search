package com.example.coursesearch.retrofit

import com.example.coursesearch.models.CourseWrapper

class ApiHelper(private val apiService: ApiService) {
    suspend fun getCourses(query: HashMap<String, String>, page: Int):CourseWrapper {
        query["page_size"] = "10"
        query["page"] = page.toString()
        return apiService.getCourses(query)
    }

    suspend fun getCourseReviews(page:Int, course_id:Int)=
        apiService.getCourseReviews(course_id, 10, page)
}