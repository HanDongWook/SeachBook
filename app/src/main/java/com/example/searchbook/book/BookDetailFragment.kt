package com.example.searchbook.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.searchbook.databinding.FragmentBookDetailBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BookDetailFragment : Fragment() {
    private val args: BookDetailFragmentArgs by navArgs()
    private val vm: BookDetailViewModel by viewModel() {
        parametersOf(args.isbn)
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.bookDetail.collectLatest {
                    with(binding) {
                        isbn.text = "ISBN : ${it.isbn}"

                        if (it.image == null) image.visibility = View.GONE
                        else Glide.with(requireContext()).load(it.image).into(image)

                        if (it.title == null) title.visibility = View.GONE
                        else title.text = "제목 : ${it.title}"

                        if (it.author == null) author.visibility = View.GONE
                        else author.text = "저자 : ${it.author}"

                        if (it.price == null) priceDiscount.visibility = View.GONE
                        else priceDiscount.text = "가격 : ${it.price}"

                        if (it.publisher == null) publisher.visibility = View.GONE
                        else publisher.text = "출판사 : ${it.publisher}"

                        if (it.pubdate == null) pubDate.visibility = View.GONE
                        else pubDate.text = "출판일 : ${it.pubdate}"

                        if (it.description == null) description.visibility = View.GONE
                        else description.text = it.description
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