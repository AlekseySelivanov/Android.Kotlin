package com.example.movieappkotlin.ui.single_movie_details

import androidx.lifecycle.LiveData
import com.example.movieappkotlin.data.api.TheMovieDBInterface
import com.example.movieappkotlin.data.repository.MovieDetailsNetworkDataSource
import com.example.movieappkotlin.data.repository.NetworkState
import com.example.movieappkotlin.data.val_objects.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService : TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)
        return movieDetailsNetworkDataSource.downloadedMovieResponse

    }
    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }
}