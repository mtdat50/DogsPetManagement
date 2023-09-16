package com.example.dogspetmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.dogspetmanagement.database.AppDatabase
import com.example.dogspetmanagement.database.DogDao
import com.example.dogspetmanagement.databinding.FragmentSecondBinding
import com.example.dogspetmanagement.model.AppViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private val sharedViewModel: AppViewModel by activityViewModels()
    private var _binding: FragmentSecondBinding? = null
    private lateinit var dogDAO: DogDao

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dogDAO = AppDatabase.getInstance(requireContext()).dogDao()

        setDetail()

        binding.saveButton.setOnClickListener {
            val editedDogBreed = binding.editDogBreed.text
            val editedDogDescription = binding.editDogDescription.text

            val dogName = binding.dogName.text

            lifecycleScope.launch {
                dogDAO.updateDog(
                    sharedViewModel.selectedDogInfo.imagePath,
                    dogName.toString(),
                    editedDogBreed.toString(),
                    editedDogDescription.toString()
                )
            }
        }
    }

    private fun setDetail() {
        // set hint
        binding.editDogBreed.hint = sharedViewModel.selectedDogInfo.breed
        binding.editDogDescription.hint = sharedViewModel.selectedDogInfo.description

        // set text
        binding.dogName.text = sharedViewModel.selectedDogInfo.name
        binding.editDogBreed.setText(sharedViewModel.selectedDogInfo.breed)
        binding.editDogDescription.setText(sharedViewModel.selectedDogInfo.description)

        // set image
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}