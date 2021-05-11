package io.smooth.store.json

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JsonModel(
    @PrimaryKey val id: Int,
    val data: String
)