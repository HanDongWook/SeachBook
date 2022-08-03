package com.example.searchbook.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchbook.databinding.FragmentBookMarkingBookBinding
import com.example.searchbook.domain.BookUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookMarkingBookFragment : Fragment() {
    private val vm: BookMarkingBookViewModel by viewModel()

    private var _binding: FragmentBookMarkingBookBinding? = null
    private val binding get() = _binding!!
    private var bookMarkingBookAdapter: BookMarkingBookAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookMarkingBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        viewModule()
        viewModelModule()
    }

    private fun init() {
        bookMarkingBookAdapter = BookMarkingBookAdapter(object : BookMarkingBookClickListener {
            override fun onClick(book: BookUiModel.Book) {
                vm.onBookClick(book)
            }
        })
    }

    private fun viewModule() {
        binding.rvFavoriteList.apply {
            adapter = bookMarkingBookAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun viewModelModule() {
        vm.fetch()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.favoriteList.collectLatest {
                    bookMarkingBookAdapter?.submitList(it)
                }
            }
        }

        vm.navigate.observe(viewLifecycleOwner) {
            when (it) {
                is BookMarkingBookViewModel.Navigate.BookDetail -> {
                    val action =
                        BookMarkingBookFragmentDirections.actionBookMarkingBookFragmentToBookDetailFragment(
                            it.book
                        )
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bookMarkingBookAdapter = null
        _binding = null
    }
}