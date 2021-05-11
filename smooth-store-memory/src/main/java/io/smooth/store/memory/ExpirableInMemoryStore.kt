package io.smooth.store.memory

import io.smooth.store.memory.cache.GenericCache
import io.smooth.store.memory.cache.impl.ExpirableCache
import io.smooth.store.memory.cache.impl.LRUCache
import java.util.concurrent.TimeUnit

class ExpirableInMemoryStore<Id,Data>(val flushInterval: Long = TimeUnit.MINUTES.toMillis(1)): InMemoryStore<Id, Data>() {
    override fun initCache(): GenericCache<Id, Data> = ExpirableCache<Id,Data>(
        MapGenericCache(),flushInterval
    )
}