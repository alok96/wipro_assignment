package com.zef.wipro.db

/**
 * This class mimics the property of Database, exposing a dummy Page Data object which holds the cached value of page
 */
class FactsDatabase {
    val factsDao: FactsDao

    init {
        factsDao = FactsDao()
    }

    companion object {
        @get:Synchronized
        var instance: FactsDatabase? = null
            get() {
                if (field == null) {
                    field = FactsDatabase()
                }
                return field
            }
    }
}