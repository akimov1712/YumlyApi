package features

import appClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.Test
import ru.topbun.module
import kotlin.test.assertEquals

class HistoryTest {

    @Test
    fun getTopQueries() = testApplication{
        application {
            module()
        }

        appClient.get("/history/top").apply {
            println(this.bodyAsText())
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}