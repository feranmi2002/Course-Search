package com.example.coursesearch.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.coursesearch.models.Course

@Dao
interface CourseDao {

    @Query("SELECT * FROM course")
    fun getAll(): LiveData<List<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg courses: Course)

    @Delete
    fun delete(course: Course)
}