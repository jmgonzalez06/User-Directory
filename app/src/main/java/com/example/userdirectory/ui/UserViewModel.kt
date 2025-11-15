package com.example.userdirectory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.data.repo.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserRepository) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    // Room is the single source of truth: we switch DAO flows based on search query
    val users: StateFlow<List<UserEntity>> =
        _query
            .flatMapLatest { q -> repo.searchFlow(q) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Show Room immediately, then try to refresh from network
        viewModelScope.launch { repo.refreshUsers() }
    }

    fun onQueryChange(newValue: String) { _query.value = newValue }
}

@Suppress("UNCHECKED_CAST")
class UserViewModelFactory(private val repo: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserViewModel(repo) as T
    }
}