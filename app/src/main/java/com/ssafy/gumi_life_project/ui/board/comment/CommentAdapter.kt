package com.ssafy.gumi_life_project.ui.board.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.gumi_life_project.R
import com.ssafy.gumi_life_project.data.model.Comment
import com.ssafy.gumi_life_project.databinding.ItemCommentBinding

class CommentAdapter :
    ListAdapter<Comment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    private var currentClickedPosition: Int? = null
    var onCommentClick: ((Comment) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)

        if (currentClickedPosition == position) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.board_light_blue
                )
            )
        } else {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.white
                )
            )
        }
    }

    fun changeCommentColor() {
        currentClickedPosition = null
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.comment = comment

            binding.imageviewReply.setOnClickListener {
                onCommentClick?.invoke(comment)

                currentClickedPosition = if (adapterPosition == currentClickedPosition) {
                    null
                } else {
                    adapterPosition
                }

                notifyDataSetChanged()
            }

            if (comment.replyList.isEmpty()) {
                binding.imageviewEnter.visibility = View.GONE
            } else {
                val replyAdapter = ReplyAdapter()
                binding.replyRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
                binding.replyRecyclerView.adapter = replyAdapter
                replyAdapter.submitList(comment.replyList)
            }
        }
    }
}

class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.commentNo == newItem.commentNo
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }
}