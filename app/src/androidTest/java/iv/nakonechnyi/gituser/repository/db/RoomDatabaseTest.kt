package iv.nakonechnyi.gituser.repository.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import iv.nakonechnyi.gituser.utils.generateUser
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {

    private lateinit var database: GitDb
    private lateinit var dao: GitUserReposDao
    private lateinit var loginsForTest: List<String>

    @Before
    fun setUp() {

        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            GitDb::class.java
        ).build()

        dao = database.gitUserReposDao()

        loginsForTest = listOf("gembrilus", "seezov", "mojombo", "defunkt", "demon")

    }

    @Test
    fun whenInsertUserThenReadTheSameOne() {

        val login = loginsForTest[0]

        val user = generateUser(login)

        runBlocking { dao.insert(user) }

        val usersFromDb = runBlocking { dao.getAllSavedUsers() }

        assertEquals(usersFromDb.size, 1)
        assertEquals(user, usersFromDb[0])

    }

    @Test
    fun whenInsertManyUsersThenReadThemAll() {

        val count = loginsForTest.size
        val addedUsers = mutableListOf<GitUserWithRepos>()

        repeat(count) {
            val user = generateUser(loginsForTest[it])
            addedUsers.add(user)
            runBlocking { dao.insert(user) }
        }

        val usersFromDb = runBlocking { dao.getAllSavedUsers() }

        assertEquals(count, usersFromDb.size)
        repeat(count) { index -> assertEquals(addedUsers[index], usersFromDb[index]) }

    }

    @Test
    fun whenDeleteUserThenReadNothing() {
        val login = loginsForTest[0]

        val user = generateUser(login)

        runBlocking { dao.insert(user) }

        val usersFromDb = runBlocking { dao.delete(login) }
        val count = runBlocking { dao.getAllSavedUsers().size }

        assertEquals(count, 0)
    }

    @Test
    fun whenDeleteNotAllUsersThenReadUsersWithoutIsDeleted() {
        val count = loginsForTest.size
        val addedUsers = mutableListOf<GitUserWithRepos>()

        repeat(count) {
            val user = generateUser(loginsForTest[it])
            addedUsers.add(user)
            runBlocking { dao.insert(user) }
        }

        repeat(2) {
            runBlocking { dao.delete(loginsForTest[it]) }
        }

        val new_count = runBlocking { dao.getAllSavedUsers().size }

        assertEquals(count-2, new_count)
    }

    @Test
    fun checkFindByLoginMethod() {
        val count = loginsForTest.size
        val loginForSearch = loginsForTest[2]
        val addedUsers = mutableListOf<GitUserWithRepos>()

        repeat(count) {
            val user = generateUser(loginsForTest[it])
            addedUsers.add(user)
            runBlocking { dao.insert(user) }
        }

        val returnedUser = runBlocking { dao.findByLogin(loginForSearch) }

        assertEquals(addedUsers[2], returnedUser)

    }

    @After
    fun tearDown() {

        database.close()

    }
}