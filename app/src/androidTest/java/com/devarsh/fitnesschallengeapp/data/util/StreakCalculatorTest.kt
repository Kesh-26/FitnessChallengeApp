package com.devarsh.fitnesschallengeapp.data.util

import android.content.Context
import androidx.room.Room
import com.devarsh.fitnesschallengeapp.data.database.AppDatabase
import com.devarsh.fitnesschallengeapp.data.entity.Progress
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class StreakCalculatorTest {
    private lateinit var db: AppDatabase
    private lateinit var calc: StreakCalculator

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, AppDatabase::class.java).build()
        calc = StreakCalculator(db.progressDao())
    }

    @After
    fun teardown() = db.close()

    @Test
    fun emptyReturnsZero() = runBlocking {
        assertEquals(0, calc.getCurrentStreak())
    }

    @Test
    fun singleDayStreak() = runBlocking {
        db.progressDao().insert(
            Progress(
                challengeId = 1,
                date = LocalDate.now().toString(),
                value = 10,
                completed = true
            )
        )
        assertEquals(1, calc.getCurrentStreak())
    }

    @Test
    fun threeDayStreak() = runBlocking {
        repeat(3) {
            val d = LocalDate.now().minusDays(it.toLong()).toString()
            db.progressDao()
                .insert(Progress(challengeId = 1, date = d, value = 10, completed = true))
        }
        assertEquals(3, calc.getCurrentStreak())
        assertEquals(3, calc.getLongestStreak())
    }

    @Test
    fun brokenStreakIsZero() = runBlocking {
        val d = LocalDate.now().minusDays(3).toString()
        db.progressDao().insert(Progress(challengeId = 1, date = d, value = 10, completed = true))
        assertEquals(0, calc.getCurrentStreak())
    }
}