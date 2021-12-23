package com.example.coursesearch.models

data class CourseReviewWrapper (
    val reviews: List<CourseReview>,
    val previous: String?,
    val next: String
)