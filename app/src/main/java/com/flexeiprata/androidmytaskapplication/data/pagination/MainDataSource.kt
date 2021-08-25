package com.flexeiprata.androidmytaskapplication.data.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.flexeiprata.androidmytaskapplication.data.models.Product
import com.flexeiprata.androidmytaskapplication.data.repository.MainRepository
import com.flexeiprata.androidmytaskapplication.utils.LOG_DEBUG
import com.flexeiprata.androidmytaskapplication.utils.PAGE_SIZE

class MainDataSource(private val repository: MainRepository, private val text: String) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = repository.getProducts(currentLoadingPageKey, text)
            val responseData = mutableListOf<Product>()
            val data = response.body()?.products ?: emptyList()
            val pagesCount = response.body()?.total_pages
            responseData.addAll(data)
            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
            val nextKey = if (currentLoadingPageKey == pagesCount) null else currentLoadingPageKey + 1
            Log.d(LOG_DEBUG, currentLoadingPageKey.toString() + "/" + response.body()?.total_pages.toString())
            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }


}