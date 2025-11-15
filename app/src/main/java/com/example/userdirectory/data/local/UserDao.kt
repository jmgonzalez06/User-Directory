package com.example.userdirectory.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("""
        SELECT * FROM users 
        WHERE LOWER(name) LIKE '%' || LOWER(:q) || '%' 
           OR LOWER(email) LIKE '%' || LOWER(:q) || '%'
        ORDER BY name ASC
    """)
    fun searchUsers(q: String): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)
}
