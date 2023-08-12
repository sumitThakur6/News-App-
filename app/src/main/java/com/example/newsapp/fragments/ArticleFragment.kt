package com.example.newsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.newsapp.MainActivity
import com.example.newsapp.R
import com.example.newsapp.viewModels.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment() {

    private val args : ArticleFragmentArgs by navArgs()
    private lateinit var viewModel : NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article, container, false)

        viewModel = (activity as MainActivity).viewModel

        val article = args.article
        val webView : WebView = view.findViewById(R.id.webView)

        webView.apply{
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

        val fab : FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener{
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }

        return view
    }

}