package io.smooth.store.memory

import io.smooth.store.memory.cache.GenericCache
import io.smooth.store.memory.cache.impl.PerpetualCache

class PerpetualInMemoryStore<Id, Data>() : InMemoryStore<Id, Data>() {
    override fun initCache(): GenericCache<Id, Data> = PerpetualCache<Id, Data>()
}