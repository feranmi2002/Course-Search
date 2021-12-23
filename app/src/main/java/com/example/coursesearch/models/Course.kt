package com.example.coursesearch.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.coursesearch.User

@Entity
data class Course(
    @PrimaryKey val id: Int?,
    @ColumnInfo(name = "created") val created: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "headline") val headline: String?,
    @ColumnInfo(name = "price") val price: String?,
    @ColumnInfo(name = "authors") var authors: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "url") val url: String?,
  //  @ColumnInfo(name = "visible_instructors") val visible_instructors: List<User>?,
    @ColumnInfo(name = "image_125_H") val image_125_H: String?,
    @ColumnInfo(name = "time") var time: String?,
)
