package com.example.dogspetmanagement

import android.R
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.dogspetmanagement.database.AppDatabase
import com.example.dogspetmanagement.database.Dog
import com.example.dogspetmanagement.database.DogDao
import com.example.dogspetmanagement.databinding.FragmentSecondBinding
import com.example.dogspetmanagement.model.AppViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private val sharedViewModel: AppViewModel by activityViewModels()
    private var _binding: FragmentSecondBinding? = null
    private lateinit var dogDAO: DogDao

    // new image data
    private var data : Intent? = null

    // path to /data/data/yourapp/app_data/imageDir
    private lateinit var cw : ContextWrapper
    // Create imageDir
    private lateinit var directory : File

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

        cw = ContextWrapper(requireContext().applicationContext)
        directory = cw.getDir("imageDir", Context.MODE_PRIVATE)

        dogDAO = AppDatabase.getInstance(requireContext()).dogDao()

        setDetail()
//        Log.d("HaoNhat", sharedViewModel.selectedDogInfo.id.toString())

        binding.chooseImageButton.setOnClickListener {
            imageChooser()
        }

        binding.saveButton.setOnClickListener {
            // get edited text
            sharedViewModel.selectedDogInfo.name = binding.editDogName.text.toString()
            sharedViewModel.selectedDogInfo.breed = binding.editDogBreed.text.toString()
            sharedViewModel.selectedDogInfo.description = binding.editDogDescription.text.toString()

            // save and change image path
            // check if user choose another image
            if (data != null
                && data!!.data != null
            ) {
                // check if previous image exist
                val preImage = File(sharedViewModel.selectedDogInfo.imagePath)
                if (preImage.exists()) {
                    // then delete it
                    val deleted = preImage.delete()
                }

                // set new image path
                val newImageName = generateUniqueID()
                val newImagePath = "$directory/$newImageName.jpg"
                sharedViewModel.selectedDogInfo.imagePath = newImagePath
//                Log.d("HaoNhat", newImagePath)

                val input = requireContext().contentResolver.openInputStream(data!!.data!!)
                val bitmapImage = BitmapFactory.decodeStream(input)
                saveToInternalStorage(bitmapImage)
            }
            // save to database
            lifecycleScope.launch {
                dogDAO.updateDog(
                    sharedViewModel.selectedDogInfo.id,
                    sharedViewModel.selectedDogInfo.imagePath,
                    sharedViewModel.selectedDogInfo.name,
                    sharedViewModel.selectedDogInfo.breed,
                    sharedViewModel.selectedDogInfo.description
                )
//                val allDogs = dogDAO.getAll()
//                for (dog in allDogs) {
//                    Log.d("HaoNhat", "${dog.uid} ${dog.imagePath} ${dog.name} ${dog.breed}")
//                }
            }
        }
    }

    private fun setDetail() {
        // set hint
        binding.editDogName.hint = sharedViewModel.selectedDogInfo.name
        binding.editDogBreed.hint = sharedViewModel.selectedDogInfo.breed
        binding.editDogDescription.hint = sharedViewModel.selectedDogInfo.description

        // set text
        binding.editDogName.setText(sharedViewModel.selectedDogInfo.name)
        binding.editDogBreed.setText(sharedViewModel.selectedDogInfo.breed)
        binding.editDogDescription.setText(sharedViewModel.selectedDogInfo.description)

        // set image if exists
        val preImage = File(sharedViewModel.selectedDogInfo.imagePath)
        if (preImage.exists()) {
            val absolutePath = preImage.absolutePath
            val bitmapImage = BitmapFactory.decodeFile(absolutePath)

            binding.dogImageView.setImageBitmap(bitmapImage)
        }
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
            data = result.data
            // do your operation from here....
            if (data != null
                && data!!.data != null
            ) {
                val selectedImageUri = data!!.data
                binding.dogImageView.setImageURI(selectedImageUri)
            }
        }
    }

    private fun generateUniqueID(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap) {
        val myPath = File(sharedViewModel.selectedDogInfo.imagePath)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(myPath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}