package com.example.searchbook.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchbook.databinding.FragmentBookSearchBinding
import com.example.searchbook.domain.BookUiModel
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

            override fun onClick(book: BookUiModel.Book) {
                vm.onBookClick(book)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModule()
        viewModelModule()
    }

    private fun viewModule() {
        pagingAdatper.addLoadStateListener { combinedLoadStates ->
            with(binding) {
                progressCircular.isVisible = combinedLoadStates.source.refresh is LoadState.Loading

                rvBookList.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading

                if (combinedLoadStates.source.refresh is LoadState.Error) {
                    showErrorDialog()
                }

                if (combinedLoadStates.source.refresh is LoadState.NotLoading
                    && combinedLoadStates.append.endOfPaginationReached
                    && pagingAdatper.itemCount < 2
                    && !pagingAdatper.isQueryEmpty()
                ) {
                    empty.visibility = View.VISIBLE
                } else {
                    empty.visibility = View.INVISIBLE
                }
            }
        }

        binding.rvBookList.apply {
            adapter = pagingAdatper
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        binding.myFavorite.setOnClickListener {
            vm.goMyFavorite()
        }
    }

    private fun viewModelModule() {
        vm.pagingList.observe(viewLifecycleOwner) { pagingData ->
            Log.d("test", "BookSearchFragment pagingdata : $pagingData ")
            pagingAdatper.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }

        vm.navigate.observe(viewLifecycleOwner) {
            when (it) {
                is BookSearchViewModel.Navigate.BookDetail -> {
                    val action =
                        BookSearchFragmentDirections.actionBookSearchFragmentToBookDetailFragment(it.book)
                    findNavController().navigate(action)
                }
                is BookSearchViewModel.Navigate.MyFavorite -> {
                    val action =
                        BookSearchFragmentDirections.actionBookSearchFragmentToBookMarkingBookFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage("Error occurred")
            .setPositiveButton("retry"
            ) { dialog, _ ->
                dialog.dismiss()
                vm.init()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}