import kotlinx.serialization.Serializable
import ru.topbun.models.tag.TagType

@Serializable
data class TagDTO(
    val id: Int,
    val type: TagType,
    val name: String,
    val icon: String
)
