package com.example.coursesearch.retrofit

class ApiHelper(private val apiService: ApiService) {
    suspend fun getCourses(query: HashMap<String, String?>, page: Int) =
        apiService.getCourses(query, 10, page)

    suspend fun getCourseReviews(page:Int, course_id:Int)=
        apiService.getCourseReviews(course_id, 10, page)
}