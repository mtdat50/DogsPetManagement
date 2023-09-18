package com.example.dogspetmanagement

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dogspetmanagement.database.AppDatabase
import com.example.dogspetmanagement.database.Dog
import com.example.dogspetmanagement.database.DogDao
import com.example.dogspetmanagement.database.User
import com.example.dogspetmanagement.database.UserDao
import com.example.dogspetmanagement.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userDAO: UserDao
    private lateinit var dogDAO: DogDao
    private var accountList = mutableListOf< Pair< String, String > >()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDAO = AppDatabase.getInstance(this).userDao()

//        generateSampleData()

        lifecycleScope.launch {
            val queryResult = userDAO.getAll()
            for (user in queryResult)
                accountList.add(Pair(user.username, user.password))
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            val password = binding.passwordInput.text.toString()
            var correct = false

            for (account in accountList)
                correct = correct || (username == account.first && password == account.second)


            if (correct) {
                binding.textView4.visibility = View.INVISIBLE
                startActivity(Intent(this, MainActivity::class.java))
            }
            else
                binding.textView4.visibility = View.VISIBLE
        }
    }

    private fun generateSampleData() {
        // path to /data/data/yourapp/app_data/imageDir
        var cw = ContextWrapper(this.applicationContext)
        var directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        var bitmapImage: Bitmap?
        var imageSavePath: String

        dogDAO = AppDatabase.getInstance(this).dogDao()

        lifecycleScope.launch {
            userDAO.insert(User("user1", "pass1"))
            userDAO.insert(User("user2", "pass2"))
            userDAO.insert(User("user3", "pass3"))


            bitmapImage = BitmapFactory.decodeResource(this@LoginActivity.resources, R.drawable.n02085620_952)
            imageSavePath = "$directory/1.jpg"
            saveToInternalStorage(bitmapImage, imageSavePath)
            dogDAO.insert(Dog(1, "dog1", imageSavePath, "Chihuahua", "description1"))

            bitmapImage = BitmapFactory.decodeResource(this@LoginActivity.resources, R.drawable.n02085936_426)
            imageSavePath = "$directory/2.jpg"
            saveToInternalStorage(bitmapImage, imageSavePath)
            dogDAO.insert(Dog(2, "dog2", imageSavePath, "Maltese", "description2"))

            bitmapImage = BitmapFactory.decodeResource(this@LoginActivity.resources, R.drawable.n02088466_4825)
            imageSavePath = "$directory/3.jpg"
            saveToInternalStorage(bitmapImage, imageSavePath)
            dogDAO.insert(Dog(3, "dog3", imageSavePath, "Bloodhound", "description3"))

            bitmapImage = BitmapFactory.decodeResource(this@LoginActivity.resources, R.drawable.n02091831_749)
            imageSavePath = "$directory/4.jpg"
            saveToInternalStorage(bitmapImage, imageSavePath)
            dogDAO.insert(Dog(4, "dog4", imageSavePath, "Saluki", "description4"))
        }
    }
    private fun saveToInternalStorage(bitmapImage: Bitmap?, path: String) {
        val myPath = File(path)
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(myPath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage?.compress(Bitmap.CompressFormat.PNG, 100, fos)
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
}
