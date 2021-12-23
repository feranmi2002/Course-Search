package com.example.coursesearch

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.coursesearch.models.Course

@Dao
interface CourseDao {

    @Query("SELECT * FROM course")
    fun getAll(): LiveData<List<Course>>

    @Insert
    fun insertAll(vararg courses: Course)

    @Delete
    fun delete(course: Course)

    @Delete
    fun deleteAll(allDAta: List<Course>)
}