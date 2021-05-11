package io.smooth.store.json

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.smooth.store.room.ModelDao
import io.smooth.store.room.RoomStore
import kotlinx.coroutines.flow.Flow

@Dao
interface JsonModelDao : ModelDao<JsonModel> {

    @Query("SELECT * FROM JsonModel")
    override fun getAll(): Flow<List<JsonModel>>

    @Query("SELECT * FROM JsonModel WHERE id = :id")
    override fun getById(id: Int): Flow<JsonModel?>

    @Query("DELETE FROM JsonModel where id = :id")
    override fun deleteById(id: Int)

    @Query("DELETE FROM JsonModel")
    override fun deleteAll()


}