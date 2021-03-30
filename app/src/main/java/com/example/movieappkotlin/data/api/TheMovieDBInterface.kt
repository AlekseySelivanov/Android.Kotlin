package com.example.movieappkotlin.data.api

import com.example.movieappkotlin.data.val_objects.MovieDetails
import com.example.movieappkotlin.data.val_objects.MovieResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {
    // https://api.themoviedb.org/3/movie/popular?api_key=2ed305044c5949802bd1bb16a189324c&page=1
    // https://api.themoviedb.org/3/movie/550?api_key=2ed305044c5949802bd1bb16a189324c
    // https://api.themoviedb.org/3/
    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>


}