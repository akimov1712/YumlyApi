package features

import appClient
import features.signUp.entity.SignUpReceive
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import ru.topbun.module
import kotlin.test.Test
import kotlin.test.assertEquals

class SignUpTest {

    @Test
    fun signUp() = testApplication {
        application {
            module()
        }

        val randomUsername = (100000..1000000).random().toString()

        val receive = SignUpReceive(
            email = "test$randomUsername@test.ru",
            username = randomUsername,
            password = "testtest"
        )

        appClient.post("/signUp"){
            setBody(receive)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }


}