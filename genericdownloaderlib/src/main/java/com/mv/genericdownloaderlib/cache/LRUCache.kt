package com.mv.genericdownloaderlib.cache

/**
 * Lrucache class to store data
 * @param minimalSize default size of LruCache
 * */
class LRUCache(private val minimalSize: Int = DEFAULT_SIZE) {
    private val keyMap = object : LinkedHashMap<Any, Any>(minimalSize, .75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Any, Any>): Boolean {
            val tooManyCachedItems = size > minimalSize
            if (tooManyCachedItems) eldestKeyToRemove = eldest.key
            return tooManyCachedItems
        }
    }

    /**
     * To store eldest item
     * */
    private var eldestKeyToRemove: Any? = null

    /**
     * Returns total size of cache
     * */
    val size: Int
        get() = keyMap.size

    /**
     * To set item into cache
     * */
    fun set(key: Any, value: Any) {
        keyMap[key] = value
        cycleKeyMap()
    }

    /**
     * To remove item from cache
     * */
    fun remove(key: Any) = keyMap.remove(key)

    /**
     * To get item from cache
     * */
    fun get(key: Any): Any? {
        return keyMap[key]
    }
    /**
     * To check if item exists in cache
     * */
    fun contains(key: Any): Boolean {
        return keyMap.containsKey(key)
    }

    /**
     * To clear the cache from memory
     * */
    fun clear() {
        keyMap.clear()
    }

    /**
     * To remove eldest item available in cache
     * */
    private fun cycleKeyMap() {
        eldestKeyToRemove?.let {
            keyMap.remove(it)
        }
        eldestKeyToRemove = null
    }

    companion object {
        private const val DEFAULT_SIZE = 50
    }
}