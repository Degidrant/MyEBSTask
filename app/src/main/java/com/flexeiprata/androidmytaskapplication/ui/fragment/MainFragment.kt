package com.flexeiprata.androidmytaskapplication.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.flexeiprata.androidmytaskapplication.MainActivity
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.data.api.ApiHelper
import com.flexeiprata.androidmytaskapplication.data.api.RetrofitBuilder
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.databinding.FragmentMainBinding
import com.flexeiprata.androidmytaskapplication.temporary.FavoritesTemp
import com.flexeiprata.androidmytaskapplication.ui.adapter.MainRecyclerAdapter
import com.flexeiprata.androidmytaskapplication.ui.base.ViewModelFactory
import com.flexeiprata.androidmytaskapplication.ui.main.MainViewModel
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.utils.Status
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainRecyclerAdapter

    private lateinit var finalList: List<Product>
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
        setViewModel()
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
                }
                catch (ex: Exception){
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
        /*viewModel.getProducts().observe(
            viewLifecycleOwner,
            {
                it?.let {
                    when (it.status) {
                        Status.SUCCESS -> {
                            //deco
                            it.data?.let { dataList ->
                                finalList = dataList.products
                                updateAdapter(finalList)
                            }
                        }
                        Status.ERROR -> {
                            //deco
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                        Status.LOADING -> {
                            //deco
                        }
                    }
                }
            }
        )*/



        FavoritesTemp.cartObserver.observe(
            viewLifecycleOwner,
            {
                binding.textViewCartSize.text = it.size.toString()
            }
        )


        viewModel.listData.observe(
            viewLifecycleOwner,
            {
                lifecycleScope.launch {
                    adapter.submitData(it)
                }
            }
        )


    }

    /*private fun updateAdapter(dataList: List<Product>) {
        adapter.apply {
            addProducts(dataList)
            notifyDataSetChanged()
        }

    }*/


    private fun setRecyclerViewUI() {
        /*adapter = MainRecyclerAdapter(arrayListOf(), lifecycleScope, findNavController(), magicLinearManager)
        binding.mainRV.apply {
            layoutManager = magicLinearManager
            adapter = this@MainFragment.adapter

        }*/

        adapter = MainRecyclerAdapter(lifecycleScope, findNavController(), magicLinearManager)
        binding.mainRV.apply {
            layoutManager = magicLinearManager
            adapter = this@MainFragment.adapter
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

    }

    private fun setViewModel() {
        viewModel =
            ViewModelProvider(this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))).get(
                MainViewModel::class.java
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}