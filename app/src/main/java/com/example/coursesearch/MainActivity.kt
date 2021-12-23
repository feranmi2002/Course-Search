package com.example.coursesearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room

class MainActivity : AppCompatActivity() {
    private lateinit var db:CourseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        db = Room.databaseBuilder(applicationContext, CourseDatabase::class.java, "course").build()
        super.onStart()
    }

    fun getDatabase() = db.courseDao()



}