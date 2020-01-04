package iv.nakonechnyi.gituser.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(
    tableName = "users",
    indices = [Index(value = ["login"], unique = true)]
)
data class GitUser(
    @PrimaryKey val id: Long,
    val login: String,
    val name: String?,
    val company: String?,
    @Json(name = "blog") val site: String?,
    val location: String?,
    val email: String?,
    val bio: String?,
    @Json(name = "public_repos")
    @ColumnInfo(name = "count_repos") val publicRepos: Int,
    val followers: Int,
    val following: Int,
    @Json(name = "avatar_url")
    @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    @Json(name = "html_url") val url: String,
    @Json(name = "created_at") val created: String,
    @Json(name = "updated_at") val updated: String
)