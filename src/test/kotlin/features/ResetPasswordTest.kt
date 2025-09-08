package features

import appClient
import features.resetPassword.entity.ResetPasswordConfirmReceive
import features.resetPassword.entity.ResetPasswordRequestReceive
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.Test
import ru.topbun.module
import kotlin.test.assertEquals

class ResetPasswordTest {

    @Test
    fun confirm() = testApplication {
        application {
            module()
        }

        val receive = ResetPasswordConfirmReceive("test@test.ru", "0000", "testtest")

        appClient.post("/reset/confirm"){
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

        val receive = ResetPasswordRequestReceive("test@test.ru")

        appClient.post("/reset/request"){
            setBody(receive)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}