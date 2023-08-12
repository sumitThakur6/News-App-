package com.example.newsapp.fragments.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ResourceCursorAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentCategoryBinding
import com.example.newsapp.databinding.FragmentCategoryTopicBinding
import com.example.newsapp.utils.Resource
import com.example.newsapp.viewModels.NewsViewModel


class CategoryTopicFragment : Fragment() {

    private val args : CategoryTopicFragmentArgs by navArgs()
    private lateinit var viewModel : NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding : FragmentCategoryTopicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCategoryTopicBinding.inflate(inflater, container, false)

         viewModel = (activity as MainActivity).viewModel
         setupRecyclerView()

        val topic = args.topic.topicText.lowercase()
        viewModel.getCategoryViseNews(topic)

        newsAdapter.setOnItemClickListener {
            val action = CategoryTopicFragmentDirections.actionCategoryTopicFragmentToArticleFragment(it)
            findNavController().navigate(action)
        }

        viewModel.categoryNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let{
                        newsAdapter.submitList(it.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let{message ->
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        return binding.root
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvCategoryNews.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }
    private fun hideProgressBar(){
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar(){
        binding.paginationProgressBar.visibility = View.VISIBLE
    }
}