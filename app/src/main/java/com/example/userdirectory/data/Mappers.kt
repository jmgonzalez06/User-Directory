package com.example.userdirectory.data

import com.example.userdirectory.data.local.UserEntity
import com.example.userdirectory.data.remote.UserDto

// Keep only what we display: id, name, email, phone
fun UserDto.toEntity() = UserEntity(
    id = id,
    name = name,
    email = email,
    phone = phone
)