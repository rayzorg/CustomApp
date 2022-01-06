package com.example.mainactivity.newsui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mainactivity.R
import com.example.mainactivity.adapter.NewsAdapter
import com.example.mainactivity.util.Constants.Companion.SEARCH_NEWS_DELAY
import com.example.mainactivity.util.Resource
import com.example.mainactivity.views.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_search_news.paginationProgressBar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment :  Fragment(R.layout.fragment_search_news) {


    lateinit var viewModel: NewsViewModel
    val TAG="SearchNewsFragment"
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

        var task:Job? = null
        etSearch.addTextChangedListener { edit->
            task?.cancel()
            task= MainScope().launch {
                delay(SEARCH_NEWS_DELAY)
                edit?.let {
                    if(edit.toString().isNotEmpty()){
                        viewModel.searchNews(edit.toString())
                    }
                }
            }


        }
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }


    private fun hideProgressBar() {
        paginationProgressBar.visibility=View.INVISIBLE
    }
    private fun showProgressBar() {
        paginationProgressBar.visibility=View.INVISIBLE
    }


    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        rvSearchNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }

}