package io.smooth.store.memory.cache
/**
 * A Generic K,V [GenericCache] defines the basic operations to a cache.
 */
interface GenericCache<K, V> {
  /**
   * The number of the items that are currently cached.
   */
  val size: Int

  /**
   * Cache a [value] with a given [key]
   */
  operator fun set(key: K, value: V)

  /**
   * Get the cached value of a given [key], or null if it's not cached or evicted.
   */
  operator fun get(key: K): V?

  /**
   * Remove the value of the [key] from the cache, and return the removed value, or null if it's not cached at all.
   */
  fun remove(key: K): V?

  fun getAll(): List<V>

  /**
   * Remove all the items in the cache.
   */
  fun clear()
}
