package io.ncorti.kotlin.template.app.data

import androidx.room.Query
import io.smooth.store.room.ModelDao
import kotlinx.coroutines.flow.Flow

interface UserDao : ModelDao<User> {

    @Query("select * from User")
    override fun getAll(): Flow<List<User>>

    @Query("select * from User where id = :id")
    override fun getById(id: Int): Flow<User?>

    @Query("delete from User where id = :id")
    override fun deleteById(id: Int)

    @Query("delete from User ")
    override fun deleteAll()

}