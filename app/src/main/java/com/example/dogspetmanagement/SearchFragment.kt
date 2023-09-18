package com.example.dogspetmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.dogspetmanagement.database.AppDatabase
import com.example.dogspetmanagement.database.Dog
import com.example.dogspetmanagement.database.DogDao
import com.example.dogspetmanagement.databinding.FragmentSearchBinding
import com.example.dogspetmanagement.model.AppViewModel
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: AppViewModel by activityViewModels()
    private lateinit var dogDAO: DogDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dogDAO = AppDatabase.getInstance(requireContext()).dogDao()

        binding.searchButton.setOnClickListener {
            // 0 1 2
            val selectedOption: Int = boolToInt(binding.radioButton2.isSelected) +
                                    boolToInt(binding.radioButton3.isSelected) * 2
            var queryResult =  listOf<Dog>()

            lifecycleScope.launch {
                when (selectedOption) {
                    0 -> queryResult = dogDAO.searchByName(binding.keywords.text.toString())
                    1 -> queryResult = dogDAO.searchByBreed(binding.keywords.text.toString())
                    2 -> queryResult = dogDAO.searchByDescription(binding.keywords.text.toString())
                }
                sharedViewModel.searchResult = sharedViewModel.loadDogList(queryResult)
                sharedViewModel.viewSearchResult = true
                findNavController().navigate(R.id.action_searchFragment_to_FirstFragment)
            }
        }

    }

    private fun boolToInt(value: Boolean): Int =
        if (value)
            1
        else
            0


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}