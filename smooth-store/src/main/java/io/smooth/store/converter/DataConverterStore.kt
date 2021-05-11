package io.smooth.store.converter

import io.smooth.store.Store
import kotlinx.coroutines.flow.*

open class DataConverterStore<Id, DataType, ConvertedData>(
    private val dataConverter: DataConverter<DataType, ConvertedData>,
    private val store: Store<Id, ConvertedData>
) : Store<Id, DataType> {

    override suspend fun save(saveDto: Store.SaveDto<Id, DataType>) =
        store.save(
            Store.SaveDto(
                saveDto.id,
                dataConverter.convert(saveDto.data)
            )
        )

    override suspend fun getById(id: Id): Flow<DataType?> =
        store.getById(id).map {
            if (it == null) null
            else dataConverter.convertReversed(it)
        }

    override suspend fun getAll(): Flow<List<DataType>> =
        store.getAll().mapNotNull { list ->
            list.map { dataConverter.convertReversed(it) }
        }

    override suspend fun deleteById(id: Id): Boolean =
        store.deleteById(id)

    override suspend fun deleteAll(): Boolean =
        store.deleteAll()

}