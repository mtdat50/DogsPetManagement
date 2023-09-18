package com.example.dogspetmanagement.model

import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.example.dogspetmanagement.database.Dog
import com.example.dogspetmanagement.database.DogDao

class AppViewModel: ViewModel() {
    private lateinit var dogDAO: DogDao

    class DogInfo(private val _id: Int = 0,
                  private val _imagePath: String = "",
                  private var _name: String = "",
                  private var _breed: String = "",
                  private var _description: String = "") {

        var id: Int = _id
        var imagePath: String = _imagePath
        var name: String = _name
        var breed: String = _breed
        var description: String = _description
    }

    var dogList = mutableListOf<DogInfo>()
    var searchResult = mutableListOf<DogInfo>()
    var selectedDogInfo = DogInfo()
    var viewSearchResult = false

    fun loadDogList(queryResult: List<Dog>): MutableList<DogInfo>  {
        val convertedList = mutableListOf<DogInfo>()
        for (dog in queryResult)
            convertedList.add(DogInfo(dog.uid, dog.imagePath, dog.name, dog.breed, dog.description))

        return convertedList
    }

}