package com.example.dogspetmanagement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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

    // constant to compare the activity result code
    private val SELECT_PICTURE = 200

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

        binding.chooseImageButton.setOnClickListener {
            imageChooser()
        }

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

    // Code to get image from gallery
    private fun imageChooser() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchSomeActivity.launch(i)
    }

    var launchSomeActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode
            == Activity.RESULT_OK
        ) {
            val data = result.data
            // do your operation from here....
            if (data != null
                && data.data != null
            ) {
                val selectedImageUri = data.data
                binding.dogImageView.setImageURI(selectedImageUri)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}