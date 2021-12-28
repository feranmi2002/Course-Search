package com.example.coursesearch.retrofit

import com.example.coursesearch.BuildConfig
import com.example.coursesearch.models.CourseReviewWrapper
import com.example.coursesearch.models.CourseWrapper
import retrofit2.http.*

interface ApiService {


    @Headers(
        "Accept: application/json, text/plain, */*",
        BuildConfig.KEY,
        "Content-Type: application/json;charset=utf-8"
    )
    @GET("courses")
    suspend fun getCourses(
        @QueryMap filter: HashMap<String, String>,
    ): CourseWrapper

//    suspend fun getCourses(
//        @QueryMap filter: HashMap<String, String>,
//        @Query("page_size") page_size: Int,
//        @Query("page") page: Int
//    ): CourseWrapper

    @Headers(
        "Accept: application/json, text/plain, */*",
        BuildConfig.KEY,
        "Content-Type: application/json;charset=utf-8"
    )
    @GET("courses/{course_id}/reviews")
    suspend fun getCourseReviews(
        @Path("course_id") course_id: Int,
        @Query("page_size") page_size: Int,
        @Query("page") page: Int,

        ): CourseReviewWrapper
}