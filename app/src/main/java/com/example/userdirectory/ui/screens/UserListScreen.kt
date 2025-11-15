package com.example.userdirectory.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.ui.UserViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun UserListScreen(viewModel: UserViewModel) {
    val users by viewModel.users.collectAsState()
    val query by viewModel.query.collectAsState()

    Scaffold(
        topBar = {
            TextField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                singleLine = true,
                placeholder = { Text("Search by name or email") }
            )
        }
    ) { inner ->
        if (users.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(inner)) {
                Text("No users yet", modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyColumn(
                contentPadding = inner,
                modifier = Modifier.fillMaxSize()
            ) {
                items(users) { user ->
                    UserRow(user)
                }
            }
        }
    }
}

@Composable
private fun UserRow(user: UserEntity) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp)) {
        Text(text = "#${user.id}  ${user.name}", style = MaterialTheme.typography.titleMedium)
        Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
        Text(text = user.phone, style = MaterialTheme.typography.bodySmall)
        Divider(Modifier.padding(top = 12.dp))
    }
}
