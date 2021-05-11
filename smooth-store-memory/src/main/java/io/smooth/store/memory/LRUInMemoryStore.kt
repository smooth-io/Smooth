package io.smooth.store.memory

import io.smooth.store.memory.cache.GenericCache
import io.smooth.store.memory.cache.impl.LRUCache

open class LRUInMemoryStore<Id, Data>(val minimalSize: Int = 10) : InMemoryStore<Id, Data>() {

    override fun initCache(): GenericCache<Id, Data> = LRUCache<Id, Data>(
        MapGenericCache(), minimalSize
    )

}