package com.example.dogspetmanagement

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.dogspetmanagement.database.AppDatabase
import com.example.dogspetmanagement.database.Dog
import com.example.dogspetmanagement.database.DogDao
import com.example.dogspetmanagement.databinding.ActivityMainBinding
import com.example.dogspetmanagement.model.AppViewModel
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: AppViewModel by viewModels()
    private lateinit var dogDAO: DogDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        dogDAO = AppDatabase.getInstance(this).dogDao()


        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        R.id.action_add -> {
            //  TODO
            // init new dog
            for (dog in sharedViewModel.dogList) {
                Log.d("HaoNhat", "${dog.id} ${dog.name}")
            }
            lifecycleScope.launch {
//                dogDAO.deleteAll()
                val lastestUID = dogDAO.getLastUID()
                val newDog = AppViewModel.DogInfo()
                if (lastestUID.isEmpty()) {
                    Log.d("HaoNhat", "the database is null")
                }
                else {
                    newDog.id = lastestUID[0] + 1
                }
                sharedViewModel.dogList.add(newDog)
                sharedViewModel.selectedDogInfo = sharedViewModel.dogList[sharedViewModel.dogList.lastIndexOf(newDog)]
                dogDAO.insert(Dog(sharedViewModel.selectedDogInfo.id, sharedViewModel.selectedDogInfo.name, sharedViewModel.selectedDogInfo.imagePath, sharedViewModel.selectedDogInfo.breed, sharedViewModel.selectedDogInfo.description))
            }
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_FirstFragment_to_SecondFragment)
            // navigate to second fragment using "add" button
            true
        }
        R.id.SearchFragment -> {
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_global_searchFragment)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}