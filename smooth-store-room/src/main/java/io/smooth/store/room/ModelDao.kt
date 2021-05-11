package io.smooth.store.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

interface ModelDao<T> {

    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T)

    fun getAll(): Flow<List<T>>

    fun getById(id: Int): Flow<T?>

    fun deleteById(id: Int)

    fun deleteAll()
}
