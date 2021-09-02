package com.flexeiprata.androidmytaskapplication.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.flexeiprata.androidmytaskapplication.R
import com.flexeiprata.androidmytaskapplication.databinding.ContactFragmentBinding
import com.flexeiprata.androidmytaskapplication.ui.adapter.ContactsAdapter
import com.flexeiprata.androidmytaskapplication.ui.main.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactFragment : Fragment() {

    private var _binding: ContactFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactViewModel by viewModels()
    private val args: ContactFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI() {
        binding.ContactsRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            lifecycleScope.launch {
                val list = viewModel.contactList
                adapter = ContactsAdapter(list).apply {
                    setShareListener {number ->
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("smsto:")
                            type = "vnd.android-dir/mms-sms"
                            putExtra("address", number)
                            putExtra("sms_body", String.format(getString(R.string.share_message), args.name, args.link))
                        }
                        startActivity(intent)
                    }
                }
            }

        }
        binding.mainToolbar.setHomeOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
