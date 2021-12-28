package com.example.coursesearch.models

import androidx.room.*

@Entity(tableName = "course", indices = [Index(value = ["id"], unique = true)])
data class Course(
    @PrimaryKey var id: Int?,
    @ColumnInfo(name = "created") var created: String?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "headline") var headline: String?,
    @ColumnInfo(name = "price") var price: String?,
    @ColumnInfo(name = "authors") var authors: String?,
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "url") var url: String?,
    @Ignore val visible_instructors: List<User>?,
    @ColumnInfo(name = "image_125_H") var image_125_H: String?,
    @ColumnInfo(name = "time") var time: String?
){
    constructor() : this(0, "", "", "",
    "", "", "", "", mutableListOf<User>(), "", "")
}
