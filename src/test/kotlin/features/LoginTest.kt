package features

import appClient
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.topbun.features.login.entity.LoginReceive
import ru.topbun.module
import kotlin.test.assertEquals

class LoginTest {

    @Test
    fun login() = testApplication {
        application {
            module()
        }

        val receive = LoginReceive("test@test.ru", "testtest")

        appClient.post("/login"){
            setBody(receive)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

}