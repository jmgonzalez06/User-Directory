@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.userdirectory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.data.repo.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val users: StateFlow<List<UserEntity>> =
        _query
            .flatMapLatest { q -> repo.searchFlow(q) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch { repo.refreshUsers() }
    }

    fun onQueryChange(newValue: String) { _query.value = newValue }

    // ← add this INSIDE the class
    fun refresh() {
        viewModelScope.launch { repo.refreshUsers() }
    }
}

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(private val repo: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repo) as T
    }
}
