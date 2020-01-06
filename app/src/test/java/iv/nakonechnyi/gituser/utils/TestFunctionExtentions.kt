package iv.nakonechnyi.gituser.utils

import iv.nakonechnyi.gituser.repository.GitUserRepository
import iv.nakonechnyi.gituser.repository.db.GitUserReposDao
import iv.nakonechnyi.gituser.repository.gitservice.GitUserInfoService
import kotlinx.coroutines.runBlocking

fun GitUserInfoService.fetchTestUser(login: String) = runBlocking {
    gitUserInfo(login)
}

fun GitUserInfoService.fetchTestRepos(login: String) = runBlocking {
    gitReposByUsername(login)
}

fun GitUserReposDao.fetchAllTestUsers() = runBlocking {
    getAllSavedUsers()
}

fun GitUserReposDao.findTestUserInDb(login: String) = runBlocking {
    findByLogin(login)
}

fun GitUserRepository.fetch(login: String) = runBlocking {
    getFullUserInfo(login)
}