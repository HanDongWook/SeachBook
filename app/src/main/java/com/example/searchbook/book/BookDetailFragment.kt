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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.searchbook.R
import com.example.searchbook.databinding.FragmentBookDetailBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BookDetailFragment : Fragment() {
    private val args: BookDetailFragmentArgs by navArgs()
    private val vm: BookDetailViewModel by viewModel() {
        parametersOf(args.book)
    }

    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModule()
        viewModelModule()
    }

    private fun viewModule() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnFavorite.setOnClickListener {
            vm.onClickFavorite()
        }
    }

    private fun viewModelModule() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.bookDetail.collectLatest {
                    with(binding) {
                        isbn.text = resources.getString(R.string.isbn, it.isbn.toString())
                        title.text = resources.getString(R.string.title, it.title)
                        author.text = resources.getString(R.string.author, it.author)

                        if (it.discount == null) priceDiscount.visibility = View.GONE
                        else priceDiscount.text = resources.getString(R.string.price, it.discount)

                        if (it.publisher == null) publisher.visibility = View.GONE
                        else publisher.text = resources.getString(R.string.publisher, it.publisher)

                        if (it.pubdate == null) pubDate.visibility = View.GONE
                        else pubDate.text = resources.getString(R.string.pubdate, it.pubdate)

                        if (it.description == null) description.visibility = View.GONE
                        else description.text = it.description

                        if (it.image == null) image.visibility = View.GONE
                        else Glide.with(requireContext()).load(it.image).into(image)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}