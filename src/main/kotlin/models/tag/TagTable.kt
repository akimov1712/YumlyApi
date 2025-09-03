package ru.topbun.models.tag

import TagDTO
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object TagTable: IntIdTable("tags") {

    val name = varchar("name", 32)
    val type = varchar("type", 24)
    val icon = text("icon")


    fun getTag(id: Int) = transaction {
        selectAll().where { TagTable.id eq id }.first().toDTO()
    }

    private fun ResultRow.toDTO() = TagDTO(
        id = this[id].value,
        type = TagType.valueOf(this[TagTable.type]),
        name = this[TagTable.name],
        icon = this[TagTable.icon]
    )


}