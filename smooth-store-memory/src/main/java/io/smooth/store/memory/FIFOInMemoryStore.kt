package io.smooth.store.memory

import io.smooth.store.memory.cache.GenericCache
import io.smooth.store.memory.cache.impl.FIFOCache
import io.smooth.store.memory.cache.impl.LRUCache

class FIFOInMemoryStore<Id, Data>(val minimalSize: Int = 10) : InMemoryStore<Id, Data>() {
    override fun initCache(): GenericCache<Id, Data> = FIFOCache<Id, Data>(
        MapGenericCache(), minimalSize
    )
}