package com.example.projectofmovies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class FragmentHome : Fragment() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    private lateinit var recView_home: RecyclerView
    private lateinit var itemAdapter: RecyclerAdapter


    override fun onCreateView(   inflater: LayoutInflater, container: ViewGroup?,
                                 savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recView_home = view.findViewById(R.id.recView_home)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Init Adapter
        itemAdapter = RecyclerAdapter()

        // Set adapter to recycler
        recView_home.apply {
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            adapter = itemAdapter
        }

    }
}