package ru.topbun.models.tag.preparation

import TagDTO
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.tag.TagType

object PreparationsTable: IntIdTable("preparations"){
    val name = text("name")
    val icon = text("icon")


    fun getTag(id: Int) = transaction {
        selectAll().where { PreparationsTable.id eq id }.first().toDTO()
    }

    private fun ResultRow.toDTO() = TagDTO(
        id = this[id].value,
        type = TagType.Preparation,
        name = this[PreparationsTable.name],
        icon = this[PreparationsTable.icon]
    )

}