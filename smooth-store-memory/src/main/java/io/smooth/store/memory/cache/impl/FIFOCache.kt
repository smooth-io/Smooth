package io.smooth.store.memory.cache.impl

import io.smooth.store.memory.cache.GenericCache

/**
 * [FIFOCache] caches at most [minimalSize] items that are recently [set].
 */
class FIFOCache<K, V>(
    private val delegate: GenericCache<K, V>,
    private val minimalSize: Int = DEFAULT_SIZE
) :
    GenericCache<K, V> by delegate {
    private val keyMap = object : LinkedHashMap<K, Boolean>(minimalSize, .75f) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, Boolean>): Boolean {
            val tooManyCachedItems = size > minimalSize
            if (tooManyCachedItems) eldestKeyToRemove = eldest.key
            return tooManyCachedItems
        }
    }

    private var eldestKeyToRemove: K? = null

    override fun set(key: K, value: V) {
        delegate[key] = value
        cycleKeyMap(key)
    }

    override fun get(key: K): V? {
        keyMap[key]
        return delegate[key]
    }

    override fun getAll(): List<V> =
        delegate.getAll()

    override fun clear() {
        keyMap.clear()
        delegate.clear()
    }

    private fun cycleKeyMap(key: K) {
        keyMap[key] = PRESENT
        eldestKeyToRemove?.let { delegate.remove(it) }
        eldestKeyToRemove = null
    }

    companion object {
        private const val DEFAULT_SIZE = 100
        private const val PRESENT = true
    }
}
