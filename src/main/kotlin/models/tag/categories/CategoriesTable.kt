package ru.topbun.models.tag.categories

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.topbun.models.category.TagDTO

object CategoriesTable: IntIdTable("categories"){
    val name = text("name")
    val icon = text("icon")

    fun getTag(id: Int) = transaction {
        selectAll().where { CategoriesTable.id eq id }.first().toDTO()
    }

    private fun ResultRow.toDTO() = TagDTO(
        id = this[id].value,
        name = this[name],
        icon = this[icon]
    )

}