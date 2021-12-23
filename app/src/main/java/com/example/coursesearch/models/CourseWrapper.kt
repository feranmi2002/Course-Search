package com.example.coursesearch.models

data class CourseWrapper(
    val count:String?,
    val next:String?,
    val previous:String?,
    var results:List<Course>
)
