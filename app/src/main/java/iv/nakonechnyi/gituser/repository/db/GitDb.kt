package iv.nakonechnyi.gituser.repository.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import iv.nakonechnyi.gituser.entities.GitRepo
import iv.nakonechnyi.gituser.entities.GitUser
import kotlinx.coroutines.CoroutineScope

@Database(entities = [GitUser::class, GitRepo::class], version = 1, exportSchema = true)
abstract class GitDb : RoomDatabase() {

    abstract fun gitUserReposDao(): GitUserReposDao

    companion object {

        @Volatile
        private var INSTANCE: GitDb? = null

        fun database(context: Context): GitDb =
            INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: Room.databaseBuilder(
                            context.applicationContext,
                            GitDb::class.java,
                            "git_user.db"
                        )
                            .build()
                            .also {
                                INSTANCE = it
                            }
                }

    }
}