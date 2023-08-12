package com.example.newsapp.fragments.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.CategoryAdapter
import com.example.newsapp.adapters.OnClickEvent
import com.example.newsapp.models.Category

class CategoryFragment : Fragment(), OnClickEvent {

    private lateinit var mAdapter : CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_category, container, false)

        val recyclerView : RecyclerView = view.findViewById(R.id.rvCategory)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        mAdapter = CategoryAdapter(requireContext(), this)
        recyclerView.adapter = mAdapter

        val list = listOf<Category>(
            Category("Sports", R.drawable.sport),
            Category("Politics", R.drawable.polictics),
            Category("Business", R.drawable.business),
            Category("Health", R.drawable.health))

        mAdapter.submitList(list)

        return view
    }

    override fun OnCategoryClick(category: Category) {
        val action = CategoryFragmentDirections.actionCategoryFragmentToCategoryTopicFragment(category)
        findNavController().navigate(action)
    }

}