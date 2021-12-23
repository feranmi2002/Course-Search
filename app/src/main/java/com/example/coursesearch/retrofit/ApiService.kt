package com.example.coursesearch.retrofit

import com.example.coursesearch.models.CourseReviewWrapper
import com.example.coursesearch.models.CourseWrapper
import retrofit2.http.*

interface ApiService {

    @Headers(
        "Accept: application/json, text/plain, */*",
        "Authorization: Basic d2w2anZHT2lXbDZ0b0xOV0ljUTFSYmpqMEZRN0xKZ3dmaHl6Ym03NjpxYzJOZlA0clc0RVJ0U2Nod2NtRVVhVzlTRGFOMHQ4QVdkSXFFV0NsUzAzdjVpQ1Z6bWE3ZDlhUlRWTzZyVlRUVDI3RTRlVktHZExkZFMzRVNpUWx4em5uSkZlenR5NjNoZnJXd2FsQkswZm9ycENNRXY5RjV1Z0xSeG5xNWp4dQ==",
        "Content-Type: application/json;charset=utf-8"
    )
    @GET("courses")
    suspend fun getCourses(
        @QueryMap filter: HashMap<String, String?>,
        @Query("page_size") page_size:Int,
        @Query("page") page:Int
    ): CourseWrapper

    @Headers(
        "Accept: application/json, text/plain, */*",
        "Authorization: Basic d2w2anZHT2lXbDZ0b0xOV0ljUTFSYmpqMEZRN0xKZ3dmaHl6Ym03NjpxYzJOZlA0clc0RVJ0U2Nod2NtRVVhVzlTRGFOMHQ4QVdkSXFFV0NsUzAzdjVpQ1Z6bWE3ZDlhUlRWTzZyVlRUVDI3RTRlVktHZExkZFMzRVNpUWx4em5uSkZlenR5NjNoZnJXd2FsQkswZm9ycENNRXY5RjV1Z0xSeG5xNWp4dQ==",
        "Content-Type: application/json;charset=utf-8"
    )
    @GET("courses/{course_id}/reviews")
    suspend fun getCourseReviews(
        @Path("course_id") course_id:Int,
        @Query("page_size") page_size:Int,
        @Query("page") page:Int,

    ):CourseReviewWrapper
}