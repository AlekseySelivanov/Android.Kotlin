package com.example.movieappkotlin.ui.popular_movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class JenreAdapter (private val list: List<Movie_genre>)
    : RecyclerView.Adapter<JenreViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JenreViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return JenreViewHolder(inflater, parent)
    }
    override fun onBindViewHolder(holder: JenreViewHolder, position: Int) {
        val movie: Movie_genre = list[position]
        holder.bind(movie)
    }
    override fun getItemCount(): Int = list.size
}