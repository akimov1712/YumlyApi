
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import ru.topbun.utills.Env

val ApplicationTestBuilder.appClient get() = createClient {
    defaultRequest {
        contentType(ContentType.Application.Json.withParameter("charset", "utf-8"))
        appendTestToken()
    }
    install(ContentNegotiation) {
        json()
    }
}

fun DefaultRequest.DefaultRequestBuilder.appendTestToken() = headers.set("Authorization", "Bearer ${Env["TEST_TOKEN"]}")