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
    data class Owner(val login: String)
}