package features

import appClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.Test
import ru.topbun.features.confirmAccount.entity.ConfirmAccountReceive
import ru.topbun.features.confirmAccount.entity.ConfirmAccountRequestReceive
import ru.topbun.module
import kotlin.test.assertEquals

class ConfirmAccountTest {

    @Test
    fun confirm() = testApplication {
        application {
            module()
        }

        val receive = ConfirmAccountReceive("test@test.ru", "0000")

        appClient.post("/verify/confirm"){
            setBody(receive)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

    }

    @Test
    fun request() = testApplication {
        application {
            module()
        }

        val receive = ConfirmAccountRequestReceive("test@test.ru")

        appClient.post("/verify/request"){
            setBody(receive)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}