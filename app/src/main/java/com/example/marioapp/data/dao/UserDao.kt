package com.example.marioapp.data.dao

import androidx.room.*
import com.example.marioapp.data.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY CASE position WHEN '  POSITION:          GK' THEN 0 WHEN '  POSITION:          DEF' THEN 1 WHEN '  POSITION:          MID' THEN 2 WHEN '  POSITION:          ATT' THEN 3  END")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE name LIKE '%' || :search || '%'")
    fun searchByName(search: String): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user : User)

    @Query("SELECT * FROM user WHERE uid = :uid")
    fun get(uid: Int) : User

    @Update
    fun update(user : User)

}