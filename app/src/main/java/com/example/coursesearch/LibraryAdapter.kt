package com.example.coursesearch

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coursesearch.databinding.SearchItemBinding
import com.example.coursesearch.models.Course

class LibraryAdapter(
    var data: List<Course>?,
    private val click: (action: String, course: Course) -> Unit
) :
    RecyclerView.Adapter<LibraryAdapter.CourseReviewHolder>() {

    inner class CourseReviewHolder(private val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: Course) {
            Log.i("CourseSearch", "Binding Called")
            with(binding) {
                Glide.with(itemView)
                    .load(currentItem.image_125_H)
                    .placeholder(R.drawable.ic_baseline_image_placeholder_24)
                    .into(image)
                title.text = currentItem.title
                authors.text = currentItem.authors
                price.text = currentItem.price
                description.text = currentItem.headline
                addToLibrary.text = "Remove From Library"
                addToLibrary.setOnClickListener {
                    click.invoke("Library", currentItem)
                }
                seeReviews.setOnClickListener {
                    click.invoke("See reviews", currentItem)
                }
                link.setOnClickListener {
                    click.invoke("Link", currentItem)
                }

            }
        }
    }

    override fun onBindViewHolder(holder: CourseReviewHolder, position: Int) {
        val currentItem = data?.get(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseReviewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseReviewHolder(binding)
    }

    fun submitData(list: List<Course>) {
        data = list
    }

    override fun getItemCount(): Int {
        return data!!.size
    }


//    override fun getItemCount(): Int {
//        return data.size
//    }


}