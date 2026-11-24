package com.devarsh.fitnesschallengeapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.devarsh.fitnesschallengeapp.data.dao.*
import com.devarsh.fitnesschallengeapp.data.entity.*

// Existing migration 1 -> 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            "ALTER TABLE challenges " +
                    "ADD COLUMN easyTarget INTEGER NOT NULL DEFAULT 0"
        )

        db.execSQL(
            "ALTER TABLE challenges " +
                    "ADD COLUMN hardTarget INTEGER NOT NULL DEFAULT 0"
        )

        db.execSQL(
            "ALTER TABLE challenges " +
                    "ADD COLUMN timeLimitSeconds INTEGER NOT NULL DEFAULT 0"
        )

        db.execSQL(
            "ALTER TABLE progress " +
                    "ADD COLUMN selectedDifficulty TEXT"
        )

        db.execSQL(
            "ALTER TABLE progress " +
                    "ADD COLUMN timeLimitSeconds INTEGER"
        )
    }
}

// Existing migration 2 -> 3
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            "ALTER TABLE users " +
                    "ADD COLUMN username TEXT NOT NULL DEFAULT ''"
        )

        db.execSQL(
            "ALTER TABLE users " +
                    "ADD COLUMN photoPath TEXT"
        )
    }
}

// New migration 3 -> 4
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS daily_steps (
                date TEXT NOT NULL,
                sensorBaseline INTEGER NOT NULL,
                currentSensorValue INTEGER NOT NULL,
                stepsToday INTEGER NOT NULL,
                PRIMARY KEY(date)
            )
            """.trimIndent()
        )
    }
}

@Database(
    entities = [
        User::class,
        Challenge::class,
        Progress::class,
        Badge::class,
        UserBadge::class,
        Settings::class,
        DailySteps::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun challengeDao(): ChallengeDao

    abstract fun progressDao(): ProgressDao

    abstract fun badgeDao(): BadgeDao

    abstract fun settingsDao(): SettingsDao

    abstract fun dailyStepsDao(): DailyStepsDao
}