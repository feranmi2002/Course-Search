package com.example.coursesearch

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coursesearch.databinding.CourseBinding
import com.example.coursesearch.databinding.ReviewItemBinding
import com.example.coursesearch.models.Course
import com.example.coursesearch.models.CourseReview

class CourseReviewAdapter() :
    PagingDataAdapter<CourseReview, CourseReviewAdapter.CourseReviewHolder>(COMPARATOR) {

    private var data: List<Course> = listOf()

    inner class CourseReviewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: CourseReview) {
            Log.i("CourseSearch", "Binding Called")
            with(binding) {
                comment.text = currentItem.content
                name.text = currentItem.user?.name
                rating.rating = (currentItem.rating?.toFloat() ?: 0).toFloat()
            }
        }
    }


    object COMPARATOR : DiffUtil.ItemCallback<CourseReview>() {
        override fun areItemsTheSame(oldItem: CourseReview, newItem: CourseReview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CourseReview, newItem: CourseReview): Boolean {
            return oldItem == newItem
        }


    }

    fun doSomething() = "jsdhskdhsjkldh"
    override fun onBindViewHolder(holder: CourseReviewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseReviewHolder {
        val binding = ReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseReviewHolder(binding)
    }

    fun submitData(list: List<Course>) {
        data = list
    }


//    override fun getItemCount(): Int {
//        return data.size
//    }


}