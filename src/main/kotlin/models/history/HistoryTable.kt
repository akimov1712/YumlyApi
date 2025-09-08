package ru.topbun.models.history

import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

object HistoryTable: IntIdTable("history") {

    val q = text("q")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    fun insert(q: String) = transaction {
        if (q.length > 2){
            insert{ it[HistoryTable.q] = q }
        }
    }

    fun getTop5Query(): List<String> = transaction {
        val threeMonthsAgo = LocalDateTime.now().minusMonths(3).toKotlinLocalDateTime()
        val countExpr = HistoryTable.id.count()

        select(q, countExpr)
            .where { createdAt greaterEq threeMonthsAgo }
            .groupBy(q)
            .orderBy(countExpr, SortOrder.DESC)
            .limit(5)
            .map { it[q] to it[countExpr] }.map { it.first }
    }
}