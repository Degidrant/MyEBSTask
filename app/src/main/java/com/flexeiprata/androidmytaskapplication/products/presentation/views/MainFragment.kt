package com.flexeiprata.androidmytaskapplication.products.presentation.views

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.common.paintFab
import com.flexeiprata.androidmytaskapplication.databinding.FragmentMainBinding
import com.flexeiprata.androidmytaskapplication.favorites.presentation.view.FavResult
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import com.flexeiprata.androidmytaskapplication.products.presentation.adapter.ProductsAdapter
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductPayloads
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*


@AndroidEntryPoint
class MainFragment : Fragment(), ProductsAdapter.FavoriteSwitch {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    private var adapter: ProductsAdapter = ProductsAdapter(this)
    private var isLoading = true

    private lateinit var magicLinearManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        magicLinearManager = GridLayoutManager(requireContext(), 1)
        setRecyclerViewUI()

        binding.apply {
            fabColumnStyle.setOnClickListener {
                switchLayoutManager(true)
            }
            fabSquareStyle.setOnClickListener {
                switchLayoutManager(false)
            }
            cartButton.setOnClickListener {
                viewModel.clearCart()
                Toast.makeText(requireContext(), "Test: Clear Cart", Toast.LENGTH_SHORT).show()
            }
            mainCustomToolbar.setOptionOnClickListener {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToFavoriteFragment())
            }
        }
        setupObserversUI()
        binding.collapser.apply {
            setExpanded(false)
            setOnClickListener {
                binding.mainRV.smoothScrollToPosition(0)
            }
        }
        //TODO: Without API working
        /*findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDescFragment(
                2
            )
        )*/
    }

    override fun onStart() {
        super.onStart()
        binding.searchBar.addTextChangedListener { editable ->
            lifecycleScope.launch {
                viewModel.initialize(editable.toString())
            }
        }
    }


    private fun switchLayoutManager(isLinear: Boolean) {
        binding.apply {
            val deco = DividerItemDecoration(
                mainRV.context,
                DividerItemDecoration.VERTICAL
            )
            magicLinearManager.spanCount = if (isLinear) {
                fabColumnStyle.paintFab(requireContext(), R.color.white, R.color.deep_blue)
                fabSquareStyle.paintFab(requireContext(), R.color.deep_blue, R.color.light_gray)
                mainRV.addItemDecoration(deco)
                1
            } else {
                fabSquareStyle.paintFab(requireContext(), R.color.white, R.color.deep_blue)
                fabColumnStyle.paintFab(requireContext(), R.color.deep_blue, R.color.light_gray)
                mainRV.removeItemDecoration(deco)
                2
            }
            adapter.notifyItemRangeChanged(0, mainRV.adapter!!.itemCount - 1)

        }
    }


    private fun setupObservers() {
        viewModel.initialize("")
        viewModel.getCart()
        lifecycleScope.launchWhenCreated {
            viewModel.state.collectLatest {
                if (it is ProductResult.Success)
                adapter.submitData(it.data)
            }
        }

    }

    private fun setupObserversUI() {
        lifecycleScope.launchWhenCreated {
            viewModel.favState.collectLatest {
                if (it is FavResult.Success){
                    val mainList = adapter.snapshot().items
                    mainList.forEach { inListItem ->
                        if (!viewModel.findItemInFav(inListItem, it.data, mainList, adapter) && inListItem.isFav) {
                            inListItem.isFav = false
                            adapter.notifyItemChanged(
                                mainList.indexOf(inListItem),
                                mutableListOf(ProductPayloads.FavChanged(false))
                            )
                        }
                    }
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.cartState.collectLatest{
                if (it is FavResult.Success)
                binding.cartButton.setCounter(it.data.size)
            }
        }
    }

    private fun setRecyclerViewUI() {
        binding.mainRV.apply {
            layoutManager = magicLinearManager
            adapter = this@MainFragment.adapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
        adapter.addLoadStateListener {
            try {
                binding.progressBar.visibility =
                    if (it.refresh == LoadState.Loading) {
                        View.VISIBLE
                    } else {
                        if (binding.searchBar.text.toString()
                                .isNotEmpty()
                        ) binding.mainRV.layoutManager?.scrollToPosition(0)
                        View.INVISIBLE
                    }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            isLoading = it.refresh == LoadState.Loading
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
        if (!isLoading)
            findNavController().navigate(
                MainFragmentDirections.actionMainFragmentToDescFragment(
                    id,
                )
            )
    }

    override fun getSpanCount() = magicLinearManager.spanCount

}