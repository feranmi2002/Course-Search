package com.example.coursesearch.models

data class CourseReviewWrapper (
    val results: List<CourseReview>,
    val previous: String?,
    val next: String
)