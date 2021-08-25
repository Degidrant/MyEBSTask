package com.flexeiprata.androidmytaskapplication.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.models.ProductUIModel
import com.flexeiprata.androidmytaskapplication.databinding.FavFragmentBinding
import com.flexeiprata.androidmytaskapplication.ui.adapter.MainRecyclerAdapter
import com.flexeiprata.androidmytaskapplication.ui.main.FavViewModel
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class FavoriteFragment : Fragment(), MainRecyclerAdapter.FavoriteSwitch {

    private var _binding: FavFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavViewModel by viewModels()
    private var adapter: MainRecyclerAdapter = MainRecyclerAdapter(this) { 1 }
    private var mainList = listOf<Product>()
    private var isRefresh = false
    private lateinit var refresher: SwipeRefreshLayout.OnRefreshListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRefresh = true
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
        setRecyclerViewUI()
        setupObservers()
        binding.cartButton.setOnClickListener {
            viewModel.clearCart()
            Toast.makeText(requireContext(), "Test: Clear Cart", Toast.LENGTH_SHORT).show()
        }
        refresher = SwipeRefreshLayout.OnRefreshListener{
            lifecycleScope.launch {
                viewModel.actualizeData(mainList)
                withContext(Dispatchers.Main) {
                    binding.swiper.isRefreshing = false
                }
            }
        }
        binding.swiper.setOnRefreshListener(refresher)

        binding.mainToolbar.setHomeOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupObservers() {
        viewModel.getCart().observe(
            viewLifecycleOwner,
            {
                binding.cartButton.setCounter(it.size)
            }
        )
        viewModel.getAllFav().observe(
            viewLifecycleOwner,
            {
                updateAdapter(it)
                mainList = it
                binding.favCountText.text = it.size.toString()
            }
        )
    }

    private fun updateAdapter(dataList: List<Product>) {
        adapter.apply {
            lifecycleScope.launch {
                if (isRefresh) {
                    binding.swiper.post {
                        binding.swiper.isRefreshing = true
                        refresher.onRefresh()
                    }
                    isRefresh = false
                }
                Log.d(LOG_DEBUG, "Here is updater UI")
                submitData(PagingData.from(dataList.map {
                    Log.d(LOG_DEBUG, "${it.name}: $${it.price}")
                    ProductUIModel(it, true)
                }))
            }
        }
    }


    private fun setRecyclerViewUI() {
        val magicLayoutManager = GridLayoutManager(requireContext(), 1)
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

    override fun deleteFav(fav: Product) {
        viewModel.deleteFav(fav)
    }

    override fun insertFav(fav: Product) {
        viewModel.insertFav(fav)
    }

    override fun addToCart(product: Product) {
        viewModel.addToCart(product)
    }

    override fun navigateToNext(id: Int) {
        lifecycleScope.launch {
            val fav = viewModel.getFavById(id).first() != null
            withContext(Dispatchers.Main) {
                findNavController().navigate(
                    FavoriteFragmentDirections.actionFavoriteFragmentToDescFragment(
                        id,
                        fav
                    )
                )
            }
        }
    }

}