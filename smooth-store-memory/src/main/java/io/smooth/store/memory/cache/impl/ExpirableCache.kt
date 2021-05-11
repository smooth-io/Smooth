package io.smooth.store.memory.cache.impl

import java.util.concurrent.TimeUnit
import io.smooth.store.memory.cache.GenericCache

/**
 * [ExpirableCache] flushes the items whose life time is longer than [flushInterval].
 */
class ExpirableCache<K, V>(
    private val delegate: GenericCache<K, V>,
    private val flushInterval: Long = TimeUnit.MINUTES.toMillis(1)
) : GenericCache<K, V> by delegate {
    private var lastFlushTime = System.nanoTime()

    override val size: Int
        get() {
            recycle()
            return delegate.size
        }

    override fun remove(key: K): V? {
        recycle()
        return delegate.remove(key)
    }

    override fun get(key: K): V? {
        recycle()
        return delegate[key]
    }

    override fun getAll(): List<V> {
        recycle()
        return delegate.getAll()
    }

    private fun recycle() {
        val shouldRecycle =
            System.nanoTime() - lastFlushTime >= TimeUnit.MILLISECONDS.toNanos(flushInterval)
        if (shouldRecycle) {
            delegate.clear()
            lastFlushTime = System.nanoTime()
        }
    }
}