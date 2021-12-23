package com.example.coursesearch.models

import com.example.coursesearch.User

data class CourseReview (
    val id:String?,
    val content:String?,
    val user: User?,
    val rating:Int?,
    val created:Int?

        )