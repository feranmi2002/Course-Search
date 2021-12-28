package com.example.coursesearch.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coursesearch.R
import com.example.coursesearch.databinding.SearchItemBinding
import com.example.coursesearch.models.Course

class CourseRecyclerAdapter(private val click: (action: String, course: Course) -> Unit) :
    PagingDataAdapter<Course, CourseRecyclerAdapter.CourseViewHolder>(SEARCH_ITEM_COMPARATOR) {

    private var data: List<Course> = listOf()

    inner class CourseViewHolder(private val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentItem: Course) {
      //      Log.i("CourseSearch", "Binding Called")
            with(binding) {
                Glide.with(itemView)
                    .load(currentItem.image_125_H)
                    .placeholder(R.drawable.ic_baseline_image_placeholder_24)
                    .into(image)
                title.text = currentItem.title
                authors.text = currentItem.authors
                price.text = currentItem.price
                description.text = currentItem.headline
                addToLibrary.setOnClickListener {
                    click.invoke("Library", currentItem)
                }
                seeReviews.setOnClickListener {
                    click.invoke("See reviews", currentItem)
                }
                link.setOnClickListener {
                    click.invoke("Link", currentItem)
                }
                description.setOnClickListener {
                    click.invoke("Description", currentItem)
                }

            }
        }
    }


    object SEARCH_ITEM_COMPARATOR : DiffUtil.ItemCallback<Course>() {

        override fun areItemsTheSame(oldItem: Course, newItem: Course) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Course, newItem: Course) =

            oldItem == newItem

    }

    fun doSomething() = "jsdhskdhsjkldh"
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    fun submitData(list: List<Course>) {
        data = list
    }


//    override fun getItemCount(): Int {
//        return data.size
//    }


}