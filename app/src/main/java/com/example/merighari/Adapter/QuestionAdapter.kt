package com.example.merighari.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.merighari.Model.Question
import com.example.merighari.Pages.GeneralQuestion
import com.example.merighari.Pages.QuestionActivity
import com.example.merighari.R

class QuestionAdapter(
    private val questionList: MutableList<Question>,
    private val onQuestionSolved: (Int) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    inner class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionText: TextView = itemView.findViewById(R.id.questionText)
        val solvedIndicator: TextView = itemView.findViewById(R.id.solvedIndicator)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val context = itemView.context
                    val intent = Intent(context, QuestionActivity::class.java)
                    intent.putExtra("question", questionList[position])
                    intent.putExtra("position", position)
                    (context as Activity).startActivityForResult(intent, 100)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_item, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionList[position]
        holder.questionText.text = question.questionText


        if (question.isSolved) {
            holder.solvedIndicator.visibility = View.VISIBLE
            holder.solvedIndicator.text = "✅ Solved"
        } else {
            holder.solvedIndicator.visibility = View.GONE
        }
    }


    override fun getItemCount(): Int {
        return questionList.size
    }


    fun markQuestionAsSolved(position: Int) {
        questionList[position].isSolved = true
        notifyItemChanged(position)
    }
}
