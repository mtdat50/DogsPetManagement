package com.example.dogspetmanagement.model

import androidx.lifecycle.ViewModel

class AppViewModel: ViewModel() {
    class DogInfo(private val _imagePath: String = "",
                  private var _name: String = "",
                  private var _breed: String = "",
                  private var _description: String = "") {

        var imagePath: String = _imagePath
        var name: String = _name
        var breed: String = _breed
        var description: String = _description
    }

    var dogList = mutableListOf<DogInfo>()
    var searchResult = mutableListOf<DogInfo>()
    var selectedDogInfo = DogInfo()

}