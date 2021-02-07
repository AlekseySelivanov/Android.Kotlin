package com.example.projectofmovies

object Movie {
    var title = "Madagascar"
    var b = 20
    fun copy(title: String = this.title, b: Int = this.b) = Genre(title, b)
}