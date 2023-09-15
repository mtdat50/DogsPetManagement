package com.example.dogspetmanagement.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey @NonNull @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "password") val lastName: String
)


@Entity
data class Dog(
    @PrimaryKey @NonNull @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "imagePath") val imagePath: String,
    @ColumnInfo(name = "breed") val breed: String,
    @ColumnInfo(name = "description") val description: String
)