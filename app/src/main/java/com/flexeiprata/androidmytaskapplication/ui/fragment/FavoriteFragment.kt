package com.flexeiprata.androidmytaskapplication.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.flexeiprata.androidmytaskapplication.MainActivity
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.models.ProductUIModel
import com.flexeiprata.androidmytaskapplication.databinding.FavFragmentBinding
import com.flexeiprata.androidmytaskapplication.ui.adapter.MainRecyclerAdapter
import com.flexeiprata.androidmytaskapplication.ui.main.FavViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment(), MainRecyclerAdapter.FavoriteSwitch {

    private var _binding: FavFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavViewModel by viewModels()
    private lateinit var adapter: MainRecyclerAdapter

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
        binding.swiper.setOnRefreshListener {
            val refresher = viewModel.getAllFav()
            refresher.observe(
                viewLifecycleOwner,
                {
                    viewModel.actualizeData(it).invokeOnCompletion { _ ->
                        updateAdapter(it)
                        binding.swiper.isRefreshing = false
                        refresher.removeObservers(viewLifecycleOwner)
                    }
                }
            )
        }
        //binding.cartButton.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.ns_cart_empty))
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
                binding.favCountText.text = it.size.toString()
                viewModel.actualizeData(it)
            }
        )
    }

    private fun updateAdapter(dataList: List<Product>) {
        adapter.apply {
            lifecycleScope.launch {
                submitData(PagingData.from(dataList.map {
                    ProductUIModel(it)
                }))
            }
        }
    }


    private fun setRecyclerViewUI() {
        val magicLayoutManager = GridLayoutManager(requireContext(), 1)
        adapter = MainRecyclerAdapter(this, findNavController(), magicLayoutManager)
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

    override fun getFavByID(id: Int): Flow<Product?> = viewModel.getFavById(id)

    override fun addToCart(product: Product) {
        viewModel.addToCart(product)
    }

}