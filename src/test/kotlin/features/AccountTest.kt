package ru.topbun.features

import appClient
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import ru.topbun.features.account.entity.UpdateAccountInfoReceive
import ru.topbun.module
import kotlin.test.assertEquals

class AccountTest {

    @Test
    fun accountInfo() = testApplication {
        application {
            module()
        }

        appClient.get("/account/info").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun updateInfo() = testApplication {
        application {
            module()
        }

        val receive = UpdateAccountInfoReceive("test")

        appClient.put("/account/info") {
            setBody(receive)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }


}