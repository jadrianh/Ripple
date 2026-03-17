package org.ripple.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        ContactEntity::class,
        PhoneNumberEntity::class,
        SocialNetworkEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RippleDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao

    companion object {
        private const val DB_NAME = "ripple.db"

        @Volatile
        private var INSTANCE: RippleDatabase? = null

        fun getInstance(appContext: Context): RippleDatabase {
            val existing = INSTANCE
            if (existing != null) return existing

            return synchronized(this) {
                val again = INSTANCE
                if (again != null) {
                    again
                } else {
                    Room.databaseBuilder(
                        appContext.applicationContext,
                        RippleDatabase::class.java,
                        DB_NAME
                    ).build().also { INSTANCE = it }
                }
            }
        }
    }
}

