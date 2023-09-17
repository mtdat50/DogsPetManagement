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

    @Query("SELECT uid FROM dog ORDER BY uid DESC LIMIT 1")
    suspend fun getLastUID(): List<Int>

    @Query("SELECT uid FROM dog ORDER BY uid DESC")
    suspend fun getAllUID(): List<Int>

    @Query("UPDATE dog SET imagePath = (:imagePath) AND name = (:dogName) AND breed = (:dogBreed) AND description = (:dogDescription) WHERE uid = (:imageID)")
    suspend fun updateDog(imageID: Int, imagePath: String, dogName: String, dogBreed: String, dogDescription: String)


    @Query("SELECT * FROM dog WHERE name like '%' || :keywords || '%'")
    suspend fun searchByName(keywords: String): List<Dog>

    @Query("SELECT * FROM dog WHERE breed like '%' || :keywords || '%'")
    suspend fun searchByBreed(keywords: String): List<Dog>

    @Query("SELECT * FROM dog WHERE description like '%' || :keywords || '%'")
    suspend fun searchByDescription(keywords: String): List<Dog>

}