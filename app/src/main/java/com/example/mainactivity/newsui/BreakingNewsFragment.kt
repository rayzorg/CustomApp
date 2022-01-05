package com.example.mainactivity.newsui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mainactivity.R
import com.example.mainactivity.views.NewsViewModel

class BreakingNewsFragment : Fragment() {

    lateinit var viewModel:NewsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel=(activity as NewsActivity).viewModel




        return inflater.inflate(R.layout.fragment_breaking_news, container, false)
    }

}