package features.upload

import io.ktor.server.application.Application
import io.ktor.server.http.content.files
import io.ktor.server.http.content.static
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.configureUploadRouting() {
    routing {
        post("/upload") {
            val controller = UploadController(call)
            controller.saveImage()
        }

        static("/drawable") {
            files("src/main/resources/drawable")
        }
    }
}