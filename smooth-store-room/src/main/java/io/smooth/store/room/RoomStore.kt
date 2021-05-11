package io.smooth.store.room

import io.smooth.store.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

open class RoomStore<T>(private val modelDao: ModelDao<T>) : Store<Int, T> {

    override suspend fun save(saveDto: Store.SaveDto<Int, T>) {
        modelDao.insert(saveDto.data)
    }

    override suspend fun getById(id: Int): Flow<T?> = modelDao.getById(id)

    override suspend fun getAll(): Flow<List<T>> = modelDao.getAll()

    override suspend fun deleteById(id: Int): Boolean {
        modelDao.deleteById(id)
        return true
    }

    override suspend fun deleteAll(): Boolean {
        modelDao.deleteAll()
        return true
    }

}