package io.smooth.store

import kotlinx.coroutines.flow.Flow

interface Store<Id, Data> {

    data class SaveDto<Id, Data>(
        val id: Id,
        val data: Data
    )

    suspend fun save(saveDto: SaveDto<Id, Data>)

    suspend fun getById(id: Id): Flow<Data?>

    suspend fun getAll(): Flow<List<Data>>

    suspend fun deleteById(id: Id): Boolean

    suspend fun deleteAll(): Boolean

}