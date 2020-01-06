package iv.nakonechnyi.gituser.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "repos")
data class GitRepo(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String?,
    @Json(name = "html_url") val url: String,
    @Json(name = "created_at") val created: String,
    @Json(name = "updated_at") val updated: String,
    val language: String?,
    val size: Int,
    @Embedded val owner: Owner
) {
    data class Owner(val login: String){
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Owner

            if (login != other.login) return false

            return true
        }

        override fun hashCode(): Int {
            return login.hashCode()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GitRepo

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (url != other.url) return false
        if (created != other.created) return false
        if (updated != other.updated) return false
        if (language != other.language) return false
        if (size != other.size) return false
        if (owner != other.owner) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + url.hashCode()
        result = 31 * result + created.hashCode()
        result = 31 * result + updated.hashCode()
        result = 31 * result + (language?.hashCode() ?: 0)
        result = 31 * result + size
        result = 31 * result + owner.hashCode()
        return result
    }


}