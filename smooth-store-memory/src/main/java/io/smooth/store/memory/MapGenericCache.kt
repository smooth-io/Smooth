package io.smooth.store.memory

import io.smooth.store.memory.cache.GenericCache


internal class MapGenericCache<K, V> : GenericCache<K, V> {

    private val cache: MutableMap<K, V> = mutableMapOf()

    override val size: Int = cache.size

    override fun clear() {
        cache.clear()
    }

    override fun get(key: K): V? {
        return cache[key]
    }

    override fun remove(key: K): V? {
        return cache.remove(key)
    }

    override fun getAll(): List<V> =
        cache.values.toList()

    override fun set(key: K, value: V) {
        cache[key] = value
    }

}