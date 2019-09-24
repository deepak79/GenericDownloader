package com.mv.genericdownloaderlib.cache

class LRUCache(private val minimalSize: Int = DEFAULT_SIZE) {
    private val keyMap = object : LinkedHashMap<Any, Any>(minimalSize, .75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<Any, Any>): Boolean {
            val tooManyCachedItems = size > minimalSize
            if (tooManyCachedItems) eldestKeyToRemove = eldest.key
            return tooManyCachedItems
        }
    }

    private var eldestKeyToRemove: Any? = null

    val size: Int
        get() = keyMap.size

    fun set(key: Any, value: Any) {
        keyMap[key] = value
        cycleKeyMap()
    }

    fun remove(key: Any) = keyMap.remove(key)

    fun get(key: Any): Any? {
        return keyMap[key]
    }

    fun contains(key: Any): Boolean {
        return keyMap.containsKey(key)
    }

    fun clear() {
        keyMap.clear()
    }

    private fun cycleKeyMap() {
        eldestKeyToRemove?.let { keyMap.remove(it) }
        eldestKeyToRemove = null
    }

    companion object {
        private const val DEFAULT_SIZE = 10
    }
}