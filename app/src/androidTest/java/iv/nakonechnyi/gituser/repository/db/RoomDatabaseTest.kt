package iv.nakonechnyi.gituser.repository.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {

    private lateinit var database: GitDb
    private lateinit var dao: GitUserReposDao

    @Before
    fun setUp() {

        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            GitDb::class.java
        ).build()

        dao = database.gitUserReposDao()

    }

    @Test
    fun whenInsertUserThenReadTheSameOne() {

    }

    @Test
    fun whenUpdateUserThenReadTheSameOne() {

    }

    @Test
    fun whenInsertManyUsersThenReadThemAll() {

    }

    @Test
    fun whenDeleteUserThenReadNothing() {

    }

    @Test
    fun checkFindByLoginMethod() {

    }

    @Test
    fun checkGetAllSavedUsersMethod() {

    }

    @After
    fun tearDown() {

        database.close()

    }
}