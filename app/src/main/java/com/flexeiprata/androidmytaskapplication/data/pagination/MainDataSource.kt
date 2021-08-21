package com.flexeiprata.androidmytaskapplication.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.repository.MainRepository

class MainDataSource(private val repository: MainRepository) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = repository.getProducts(currentLoadingPageKey)
            val responseData = mutableListOf<Product>()
            val data = response.body()?.products ?: emptyList()
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        //Ia ne znaiu chto eto
        return 0
    }


}