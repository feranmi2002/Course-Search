package com.example.coursesearch.models

data class Aggregation(
    val id:String,
    val title:String?,
    val options:List<AggregationObject>
)
