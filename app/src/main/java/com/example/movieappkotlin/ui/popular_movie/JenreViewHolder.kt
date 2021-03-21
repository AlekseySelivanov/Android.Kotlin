package com.example.movieappkotlin.ui.popular_movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movieappkotlin.R

class JenreViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_genre, parent, false)) {
    private var mTitleView: TextView? = null
    init {
        mTitleView = itemView.findViewById(R.id.item_check_box)

    }
    fun bind(movie: Movie_genre) {
        mTitleView?.text = movie.genre

    }
}