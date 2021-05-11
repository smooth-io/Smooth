package io.smooth.store.memory

import io.smooth.store.Store
import io.smooth.store.memory.cache.GenericCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

abstract class InMemoryStore<Id, Data> : Store<Id, Data> {

    protected val cache: GenericCache<Id, Data> = initCache()

    protected abstract fun initCache(): GenericCache<Id, Data>

    private var dataFlow: MutableSharedFlow<List<Data>> = MutableSharedFlow(1)
    private var itemsFlow: MutableMap<Id, MutableSharedFlow<Data?>> = mutableMapOf()

    override suspend fun save(saveDto: Store.SaveDto<Id, Data>) {
        cache[saveDto.id] = saveDto.data
        updateAll()
        update(saveDto.id, saveDto.data)
    }

    override suspend fun getById(id: Id): Flow<Data?> {
        var flow = itemsFlow[id]
        if (flow == null) {
            flow = MutableSharedFlow(1)
            itemsFlow[id] = flow
        }
        flow.emit(cache[id])
        return flow
    }

    override suspend fun getAll(): Flow<List<Data>> {
        updateAll()
        return dataFlow
    }

    override suspend fun deleteById(id: Id): Boolean {
        val result = cache.remove(id) != null
        updateAll()
        return result
    }

    override suspend fun deleteAll(): Boolean {
        cache.clear()
        updateAll()
        return true
    }

    private suspend fun update(id: Id, data: Data) {
        var flow = itemsFlow[id]
        if (flow == null) {
            flow = MutableSharedFlow()
            itemsFlow[id] = flow
        }
        flow.emit(data)
    }

    private suspend fun updateAll() {
        dataFlow.emit(cache.getAll())
    }

}