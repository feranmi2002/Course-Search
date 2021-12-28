package com.example.coursesearch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coursesearch.databinding.LoadStateFooterBinding

class CourseSearchLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<CourseSearchLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(
        private val binding: LoadStateFooterBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                buttonRetry.setOnClickListener {
                    retry.invoke()
                }
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressCircular.isVisible = loadState is LoadState.Loading
                errorTextView.isVisible = loadState is LoadState.Error
                buttonRetry.isVisible = loadState is LoadState.Error
            }
        }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            LoadStateFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding, retry)
    }
}