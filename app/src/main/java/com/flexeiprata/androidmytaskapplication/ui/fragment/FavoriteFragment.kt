package com.flexeiprata.androidmytaskapplication.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.flexeiprata.androidmytaskapplication.MainActivity
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.data.api.ApiHelper
import com.flexeiprata.androidmytaskapplication.data.api.RetrofitBuilder
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.databinding.FavFragmentBinding
import com.flexeiprata.androidmytaskapplication.temporary.FavoritesTemp
import com.flexeiprata.androidmytaskapplication.ui.adapter.MainRecyclerAdapter
import com.flexeiprata.androidmytaskapplication.ui.base.FavViewModelFactory
import com.flexeiprata.androidmytaskapplication.ui.main.FavViewModel
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FavFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavViewModel
    private lateinit var adapter: MainRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setRecyclerViewUI()
        setupObservers()
        (activity as MainActivity).supportActionBar?.let {
            it.apply {
                title = ""
                setHomeAsUpIndicator(R.drawable.ns_arrow_back)
                setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            FavViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(
            FavViewModel::class.java
        )
    }

    private fun setupObservers() {
        updateAdapter(FavoritesTemp.favoriteList)
        FavoritesTemp.cartObserver.observe(
            viewLifecycleOwner,
            {
                binding.textViewCartSize.text = it.size.toString()
            }
        )
        FavoritesTemp.favObserver.observe(
            viewLifecycleOwner,
            {
                binding.favCountText.text = it.size.toString()
                updateAdapter(FavoritesTemp.favoriteList)
            }
        )

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu_favorite, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favourite -> {

            }
            else -> {
                findNavController().popBackStack()
            }
        }
        return true
    }

    private fun updateAdapter(dataList: List<Product>) {
        adapter.apply {
            lifecycleScope.launch {
                submitData(PagingData.from(dataList))
            }
        }
    }


    private fun setRecyclerViewUI() {
        val magicLayoutManager = GridLayoutManager(requireContext(), 1)
        adapter = MainRecyclerAdapter(lifecycleScope, findNavController(), magicLayoutManager)
        binding.mainRV.apply {
            layoutManager = magicLayoutManager
            adapter = this@FavoriteFragment.adapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}