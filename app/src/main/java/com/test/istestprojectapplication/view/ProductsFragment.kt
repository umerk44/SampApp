package com.test.istestprojectapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.istestprojectapplication.ISApplication
import com.test.istestprojectapplication.R
import com.test.istestprojectapplication.core.RemoteCallState
import com.test.istestprojectapplication.data.remote.model.ProductListResponse
import com.test.istestprojectapplication.view.adapter.ProductListAdapter
import com.test.istestprojectapplication.viewmodel.ProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_products.*
import javax.inject.Inject


@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val viewModel : ProductsViewModel by viewModels()


    private val productListAdapter : ProductListAdapter = ProductListAdapter(emptyList())



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        products.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productListAdapter
        }
        setUpObserver();

        (activity as MainActivity).setStandardActionBar(getString(R.string.detail_of_item_list))
        viewModel.getProducts()

    }

    private fun setUpObserver() {
        viewModel.productsView.observe(viewLifecycleOwner, { showData(it)} )
    }

    private fun showData(callState: RemoteCallState<ProductListResponse>?) {
        when(callState) {
             is RemoteCallState.Loading -> progress.visibility = VISIBLE
             is RemoteCallState.Failed -> {
                 progress.visibility = GONE
                 viewModel.getProductsFromDb()
             }
             is RemoteCallState.Success -> {
                 progress.visibility = GONE
                 productListAdapter.setData(callState.data.products)
             }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}