package com.example.searchbook.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchbook.databinding.FragmentBookSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookSearchFragment : Fragment() {

    private val vm: BookSearchViewModel by viewModel()

    private var _binding: FragmentBookSearchBinding? = null
    private val binding get() = _binding!!

    private val pagingAdatper: BookPagingAdapter by lazy {
        BookPagingAdapter(object : SearchBookListener {
            override fun onSearch(query: String) {
                vm.searchBook(query)
            }

            override fun onClick(isbn: Long) {
                vm.onBookClick(isbn)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvBookList.apply {
            adapter = pagingAdatper
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        pagingAdatper.addLoadStateListener { combinedLoadStates ->
            if (combinedLoadStates.source.refresh is LoadState.NotLoading
                && combinedLoadStates.append.endOfPaginationReached
                && pagingAdatper.itemCount < 1
            ) {
                Log.d("test", "empty : ${pagingAdatper.itemCount}")
            } else {
                Log.d("test", "adapter.itemCount : ${pagingAdatper.itemCount}")
            }
        }

        vm.pagingList.observe(viewLifecycleOwner) {
            Log.d("test", "BookSearchFragment pagingdata : $it ")
            pagingAdatper.submitData(viewLifecycleOwner.lifecycle, it)
        }

        vm.searchBook("")

        vm.navigate.observe(viewLifecycleOwner) {
            when (it) {
                is BookSearchViewModel.Navigate.BookDetail -> {
                    val action =
                        BookSearchFragmentDirections.actionBookSearchFragmentToBookDetailFragment(it.isbn)
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}