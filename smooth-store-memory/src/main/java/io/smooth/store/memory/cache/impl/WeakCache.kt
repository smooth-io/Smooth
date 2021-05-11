package io.smooth.store.memory.cache.impl

import io.smooth.store.memory.cache.Cache
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

/**
 * [WeakCache] caches items with a [WeakReference] wrapper.
 */
class WeakCache(private val delegate: Cache) : Cache by delegate {
  private val referenceQueue = ReferenceQueue<Any>()

  private class WeakEntry internal constructor(
      internal val key: Any,
      value: Any,
      referenceQueue: ReferenceQueue<Any>) : WeakReference<Any>(value, referenceQueue)

  override fun set(key: Any, value: Any) {
    removeUnreachableItems()
    val weakEntry = WeakEntry(key, value, referenceQueue)
    delegate[key] = weakEntry
  }

  override fun remove(key: Any) {
    delegate.remove(key)
    removeUnreachableItems()
  }

  override fun get(key: Any): Any? {
    val weakEntry = delegate[key] as WeakEntry?
    weakEntry?.get()?.let { return it }
    delegate.remove(key)
    return null
  }

  override fun getAll(): List<Any> =
    delegate.getAll()

  private fun removeUnreachableItems() {
    var weakEntry = referenceQueue.poll() as WeakEntry?
    while (weakEntry != null) {
      val key = weakEntry.key
      delegate.remove(key)
      weakEntry = referenceQueue.poll() as WeakEntry?
    }
  }
}