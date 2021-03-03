package com.example.movieappkotlin.ui.popular_movie

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.movieappkotlin.R
import com.example.movieappkotlin.data.api.TheMovieDBClient
import com.example.movieappkotlin.data.api.TheMovieDBInterface
import com.example.movieappkotlin.data.repository.NetworkState
import com.example.movieappkotlin.data.val_objects.MovieDetails
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private var url = ""
    private var nextToken = ""
    private lateinit var postArrayList: ArrayList<MovieDetails>
    private lateinit var adapterPost: PopularMoviePagedListAdapter
    private lateinit var progressDialog: ProgressDialog
    private var isSearch = false


    private lateinit var viewModel: MainActivityViewModel
    lateinit var movieRepository: MoviePagedListRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getViewModel()
        val movieAdapter = PopularMoviePagedListAdapter(this)
        val gridLayoutManager = GridLayoutManager(this, 3)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please, wait...")

        postArrayList = ArrayList()
        postArrayList.clear()






        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return  1
                else return 3
            }
        };

        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }
    private fun getViewModel(): MainActivityViewModel {
        isSearch = false
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search,menu)

        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchItem.collapseActionView()

                nextToken = ""
                url = ""
                postArrayList = ArrayList()
                postArrayList.clear()
                if (query != null) {
                    searchPosts(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return true
            }

        })
        return true
        }

    private fun searchPosts(query: String){
        isSearch = true
        progressDialog.show()
        url = when(nextToken){
            ""-> {
                ("https://api.themoviedb.org/3/search/keyword?q=$query&api_key=2ed305044c5949802bd1bb16a189324c&page=1")
            }
            "end" -> {
                progressDialog.dismiss()
                return
            } else -> {
                ("https://api.themoviedb.org/3/search/keyword?q=$query&api_key=2ed305044c5949802bd1bb16a189324c&page=1")
            }
        }
        val stringRequest = StringRequest(com.android.volley.Request.Method.GET, url, { response ->
            progressDialog.dismiss()
            try {
                val jsonObject = JSONObject(response)
                try {
                    nextToken = jsonObject.getString("nextPageToken")
                }catch (e:Exception){
                    nextToken = "end"
                }
                val jsonArray = jsonObject.getJSONArray("items")
                for(i in 0 until jsonArray.length()){
                    try {
                        val jsonObject01 = jsonArray.getJSONObject(i)
                        val budget = jsonObject01.getInt("budget");
                        val id = jsonObject01.getInt("id");
                        val overview = jsonObject01.getString("overview");


//   val movieDetails = MovieDetails(
//       "$budget",
//       "$id"
//   "$overview"
//   )
                    }catch (e:Exception){
                    }
                }
                adapterPost = PopularMoviePagedListAdapter(this@MainActivity)

                rv_movie_list.adapter = adapterPost
                progressDialog.dismiss()
            }catch (e:Exception){

            }
        }, {error->

        })
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val i = Intent(this@MainActivity, MainActivity::class.java)
                startActivity(i)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                val fragment = FragmentSettings()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment, fragment.javaClass.getSimpleName())
                    .commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false

    }

    }

