package com.example.fitpeo.presentation.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.fitpeo.databinding.ItemSearchSuggestionBinding

class SearchSuggestionsAdapter(
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SearchSuggestionsAdapter.SuggestionViewHolder>() {
    private var layoutInflater: LayoutInflater? = null
    private var suggestions: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        val b = ItemSearchSuggestionBinding.inflate(layoutInflater!!, parent, false)
        return SuggestionViewHolder(b)
    }
    fun clear() {
        val diffResult = DiffUtil.calculateDiff(SuggestionDiffCallback(suggestions, emptyList()))
        suggestions = emptyList()
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val suggestion = suggestions[position]
        holder.bind(suggestion)
    }

    override fun getItemCount(): Int = suggestions.size

    fun submitList(newSuggestions: List<String>) {
        val diffResult = DiffUtil.calculateDiff(SuggestionDiffCallback(suggestions, newSuggestions))
        suggestions = newSuggestions
        diffResult.dispatchUpdatesTo(this)
    }

    inner class SuggestionViewHolder(val b: ItemSearchSuggestionBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(suggestion: String) {
            b.suggestionTextView.setText(suggestion)
            itemView.setOnClickListener {
                onItemClick(suggestion)
            }
        }
    }

    // Custom DiffUtil.Callback implementation
    private class SuggestionDiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
