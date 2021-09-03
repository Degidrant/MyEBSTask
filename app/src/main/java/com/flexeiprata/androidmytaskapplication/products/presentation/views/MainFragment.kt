package com.flexeiprata.androidmytaskapplication.products.presentation.views

import android.content.res.ColorStateList
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
import com.flexeiprata.androidmytaskapplication.databinding.FragmentMainBinding
import com.flexeiprata.androidmytaskapplication.products.data.models.Product
import com.flexeiprata.androidmytaskapplication.products.presentation.adapter.ProductsAdapter
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductPayloads
import com.flexeiprata.androidmytaskapplication.products.presentation.uimodels.ProductUIModel
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
    private var favList = listOf<Product>()

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
        findNavController().navigate(
            MainFragmentDirections.actionMainFragmentToDescFragment(
                2
            )
        )
    }

    override fun onStart() {
        super.onStart()
        binding.searchBar.addTextChangedListener { editable ->
            lifecycleScope.launch {
                val flow = viewModel.listData(editable.toString(), favList)
                flow.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }


    private fun switchLayoutManager(isLinear: Boolean) {
        binding.apply {
            magicLinearManager.spanCount = if (isLinear) {
                fabColumnStyle.backgroundTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.deep_blue))
                fabColumnStyle.imageTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.white))
                fabSquareStyle.backgroundTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.light_gray))
                fabSquareStyle.imageTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.deep_blue))
                mainRV.addItemDecoration(
                    DividerItemDecoration(
                        mainRV.context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                1
            } else {
                fabSquareStyle.backgroundTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.deep_blue))
                fabSquareStyle.imageTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.white))
                binding.fabColumnStyle.backgroundTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.light_gray))
                fabColumnStyle.imageTintList =
                    ColorStateList.valueOf(requireContext().getColor(R.color.deep_blue))
                try {
                    mainRV.removeItemDecoration(mainRV.getItemDecorationAt(0))
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                2
            }
            adapter.notifyItemRangeChanged(0, mainRV.adapter!!.itemCount - 1)
        }
    }


    private fun setupObservers() {
        var init = Job() as Job
        init = lifecycleScope.launchWhenCreated {
            viewModel.getAllFav().collect {
                favList = it
                init.cancel()
            }
        }
        lifecycleScope.launchWhenCreated {
            init.join()
            viewModel.listData("", favList).collect {
                adapter.submitData(it)
            }
        }
    }

    private fun setupObserversUI() {
        lifecycleScope.launchWhenCreated {
            try {
                viewModel.getAllFav().collect { favList ->
                    val mainList = adapter.snapshot().items
                    mainList.forEach { inListItem ->
                        if (!findItemInFav(inListItem, favList, mainList) && inListItem.isFav) {
                            inListItem.isFav = false
                            adapter.notifyItemChanged(
                                mainList.indexOf(inListItem),
                                mutableListOf(ProductPayloads.FavChanged(false))
                            )
                        }
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            viewModel.getCart().collectLatest {
                binding.cartButton.setCounter(it.size)
            }
        }

    }

    private fun findItemInFav(
        inListItem: ProductUIModel,
        favList: List<Product>,
        mainList: List<ProductUIModel>
    ): Boolean {
        var guarantee = false
        favList.forEach { inFavItem ->
            if (inListItem.product.id == inFavItem.id && !inListItem.isFav) {
                inListItem.isFav = true
                adapter.notifyItemChanged(
                    mainList.indexOf(inListItem),
                    mutableListOf(ProductPayloads.FavChanged(true))
                )
                return true
            }
            if (inListItem.product.id == inFavItem.id) guarantee = true
        }
        return guarantee
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
                        if (binding.searchBar.text.toString().isNotEmpty()) binding.mainRV.layoutManager?.scrollToPosition(0)
                        View.INVISIBLE
                    }
            } catch (ex: Exception){
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