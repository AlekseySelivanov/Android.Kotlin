package com.example.projectofmovies

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment

class ChapterFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_search, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater?.inflate(R.menu.menu_search, menu)
        val searchView =
            SearchView((context as MainActivity).supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.search_view).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView.setOnClickListener { view ->  }

    }

}
