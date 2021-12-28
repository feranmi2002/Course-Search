package com.example.coursesearch.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.coursesearch.databinding.ReviewItemBinding
import com.example.coursesearch.models.Course
import com.example.coursesearch.models.CourseReview

class CourseReviewAdapter() :
    PagingDataAdapter<CourseReview, CourseReviewAdapter.CourseReviewHolder>(COMPARATOR) {

    inner class CourseReviewHolder(private val binding: ReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val defaultRating:Float = 0.0F

        fun bind(currentItem: CourseReview) {
            Log.i("CourseSearch", "Binding Called")
            with(binding) {
                comment.text = currentItem.content
                name.text = currentItem.user?.name
                rating.rating = currentItem.rating?: defaultRating
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



//    override fun getItemCount(): Int {
//        return data.size
//    }


}