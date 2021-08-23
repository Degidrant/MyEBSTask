package com.flexeiprata.androidmytaskapplication.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.flexeiprata.androidmytaskapplication.MainActivity
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.databinding.FragmentMainBinding
import com.flexeiprata.androidmytaskapplication.ui.adapter.MainRecyclerAdapter
import com.flexeiprata.androidmytaskapplication.ui.main.MainViewModel
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(), MainRecyclerAdapter.FavoriteSwitch {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: MainRecyclerAdapter

    private lateinit var magicLinearManager: GridLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        setupObservers()
        (activity as MainActivity).supportActionBar?.let {
            it.apply {
                title = ""
                setHomeAsUpIndicator(R.drawable.ns_profile)
                setDisplayHomeAsUpEnabled(true)
            }
        }
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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favourite -> {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToFavoriteFragment())
            }
            else -> {
                //profile
            }
        }
        return true
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
                fabColumnStyle.backgroundTintList =
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
            Log.d(
                LOG_DEBUG,
                "Vo = ${magicLinearManager.spanCount}, A = ${mainRV.adapter!!.itemCount - 1}"
            )
        }
    }


    private fun setupObservers() {


        viewModel.getCart().observe(
            viewLifecycleOwner,
            Observer { binding.textViewCartSize.text = it.size.toString() }
        )


        viewModel.listData.observe(
            viewLifecycleOwner,
            Observer {
                lifecycleScope.launch {
                    adapter.submitData(it)
                }
            }
        )


    }

    private fun setRecyclerViewUI() {
        adapter = MainRecyclerAdapter(this, findNavController(), magicLinearManager)
        binding.mainRV.apply {
            layoutManager = magicLinearManager
            adapter = this@MainFragment.adapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
        adapter.addLoadStateListener {
            binding.progressBar.visibility =
                if (it.refresh == LoadState.Loading)
                    View.VISIBLE
                else View.INVISIBLE
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

    override fun getFavByID(id: Int): Flow<Product?> {
        return viewModel.getFavById(id)
    }

    override fun addToCart(product: Product) {
        viewModel.addToCart(product)
    }
}