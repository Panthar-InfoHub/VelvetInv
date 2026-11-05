package org.sharad.velvetinvestment.shared.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

/** The app's local database. Everything on the server stays on the server; this is cache only. */
@Database(
    entities = [RecentSearchEntity::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(VelvetDatabaseConstructor::class)
abstract class VelvetDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao
}

/**
 * KSP generates the `actual` for this on every target, which is why the body is empty and the
 * "no actual" warning is suppressed — the compiler cannot see the generated declarations yet.
 */
@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
expect object VelvetDatabaseConstructor : RoomDatabaseConstructor<VelvetDatabase> {
    override fun initialize(): VelvetDatabase
}

internal const val DATABASE_FILE_NAME = "velvet.db"
