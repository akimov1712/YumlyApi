package ru.topbun.models.tag.diets

import TagDTO
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.tag.TagType

object DietsTable: IntIdTable("diets"){
    val name = text("name")
    val icon = text("icon")


    fun getTag(id: Int) = transaction {
        selectAll().where { DietsTable.id eq id }.first().toDTO()
    }

    private fun ResultRow.toDTO() = TagDTO(
        id = this[id].value,
        type = TagType.Diets,
        name = this[DietsTable.name],
        icon = this[DietsTable.icon]
    )

}