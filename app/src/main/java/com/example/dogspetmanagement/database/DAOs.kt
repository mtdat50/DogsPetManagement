package com.example.dogspetmanagement.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>
}

@Dao
interface DogDao {
    @Insert
    suspend fun insert(dog: Dog)

    @Delete
    suspend fun delete(dog: Dog)

    @Query("DELETE FROM dog WHERE 1=1")
    suspend fun deleteAll()

    @Query("SELECT * FROM dog")
    suspend fun getAll(): List<Dog>

    @Query("UPDATE dog SET imagePath = (:imagePath), name = (:dogName), breed = (:dogBreed), description = (:dogDescription) WHERE uid = (:imageID)")
    suspend fun updateDog(imageID: Int, imagePath: String, dogName: String, dogBreed: String, dogDescription: String)


    @Query("SELECT * FROM dog WHERE name like '%' || :keywords || '%'")
    suspend fun searchByName(keywords: String): List<Dog>

    @Query("SELECT * FROM dog WHERE breed like '%' || :keywords || '%'")
    suspend fun searchByBreed(keywords: String): List<Dog>

    @Query("SELECT * FROM dog WHERE description like '%' || :keywords || '%'")
    suspend fun searchByDescription(keywords: String): List<Dog>

}