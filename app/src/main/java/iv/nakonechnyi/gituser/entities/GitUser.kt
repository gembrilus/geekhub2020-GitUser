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
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GitUser

        if (id != other.id) return false
        if (login != other.login) return false
        if (name != other.name) return false
        if (company != other.company) return false
        if (site != other.site) return false
        if (location != other.location) return false
        if (email != other.email) return false
        if (bio != other.bio) return false
        if (publicRepos != other.publicRepos) return false
        if (followers != other.followers) return false
        if (following != other.following) return false
        if (avatarUrl != other.avatarUrl) return false
        if (url != other.url) return false
        if (created != other.created) return false
        if (updated != other.updated) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + login.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (company?.hashCode() ?: 0)
        result = 31 * result + (site?.hashCode() ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (bio?.hashCode() ?: 0)
        result = 31 * result + publicRepos
        result = 31 * result + followers
        result = 31 * result + following
        result = 31 * result + avatarUrl.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + created.hashCode()
        result = 31 * result + updated.hashCode()
        return result
    }
}