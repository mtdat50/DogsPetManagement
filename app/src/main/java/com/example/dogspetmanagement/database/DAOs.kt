package com.example.dogspetmanagement.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>
}

@Dao
interface DogDao {
    @Insert
    suspend fun insert(dog: Dog)

    @Delete
    suspend fun delete(dog: Dog)

    @Query("SELECT * FROM dog")
    suspend fun getAll(): List<Dog>

    @Query("SELECT * FROM dog WHERE name = (:dogName)")
    suspend fun getByName(dogName: String): List<Dog>

    @Query("UPDATE dog SET name = (:dogName) AND breed = (:dogBreed) AND description = (:dogDescription) WHERE imagePath = (:imagePath)")
    suspend fun updateDog(imagePath: String, dogName: String, dogBreed: String, dogDescription: String)


}