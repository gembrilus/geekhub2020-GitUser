package iv.nakonechnyi.gituser.utils

import iv.nakonechnyi.gituser.entities.GitRepo
import iv.nakonechnyi.gituser.entities.GitUser
import iv.nakonechnyi.gituser.repository.db.GitUserWithRepos
import java.lang.Math.random

private var ID = 0L

private var REPO_ID = 0

fun generateUser(login: String): GitUserWithRepos {
    val user = GitUser(
        id = ID++,
        login = login,
        name = login.capitalize(),
        company = null,
        location = null,
        bio = null,
        url = "https://github.com/users/$login",
        avatarUrl = "https://github.com/users/$login/avatar.html",
        created = "2007-10-20T05:24:19Z",
        updated = "2008-01-29T00:59:19Z",
        followers = (1000 * random()).toInt(),
        following = (100 * random()).toInt(),
        publicRepos = (200 * random()).toInt(),
        site = null,
        email = null
    )

    val repos = mutableListOf<GitRepo>()
    val count = (200 * random()).toInt()

    repeat(count) {

        repos.add(
            GitRepo(
                id = REPO_ID++,
                name = "$login-repo-$REPO_ID",
                description = null,
                url = "https://github.com/users/$login/repos",
                created = "2007-10-20T05:24:19Z",
                updated = "2008-01-29T00:59:19Z",
                language = "kotlin",
                size = (100000 * random()).toInt(),
                owner = GitRepo.Owner(login)
            )
        )
    }

    return GitUserWithRepos(user, repos)
}