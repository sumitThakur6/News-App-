package com.example.newsapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.adapters.onClickItems
import com.example.newsapp.apiServices.RetrofitHelper
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.models.Article
import com.example.newsapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp.utils.Resource
import com.example.newsapp.viewModels.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class BreakingNewsFragment : Fragment() {
    lateinit var viewModel : NewsViewModel
    lateinit var binding : FragmentBreakingNewsBinding
    lateinit var newsAdapter : NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)

        viewModel = (activity as MainActivity).viewModel
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener{
            val action = BreakingNewsFragmentDirections.actionBreakingNewsFragment2ToArticleFragment(it)
            findNavController().navigate(action)
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {newsResponse ->
                        newsAdapter.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if(isLastPage) {
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let{message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading ->{
                    showProgressBar()
                }
            }
        })
        return binding.root
    }

    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManger = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManger.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManger.childCount
            val totalItemCount = layoutManger.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling = false
            }

        }
    }

    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
}