package com.example.coursesearch.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coursesearch.models.Course

@Database(entities = [Course::class], version = 1)
abstract class CourseDatabase: RoomDatabase() {
    abstract  fun courseDao() : CourseDao
}