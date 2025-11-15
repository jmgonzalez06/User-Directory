package com.example.userdirectory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.userdirectory.data.local.AppDatabase
import com.example.userdirectory.data.repo.UserRepository
import com.example.userdirectory.ui.UserViewModel
import com.example.userdirectory.ui.UserViewModelFactory
import com.example.userdirectory.ui.screens.UserListScreen
import com.example.userdirectory.ui.theme.UserDirectoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Build Room DB
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "userdb"
        ).build()

        // Repo + ViewModel
        val repo = UserRepository(db.userDao())
        val vmFactory = UserViewModelFactory(repo)
        val viewModel = ViewModelProvider(this, vmFactory)[UserViewModel::class.java]

        setContent {
            UserDirectoryTheme {
                UserListScreen(viewModel)
            }
        }
    }
}