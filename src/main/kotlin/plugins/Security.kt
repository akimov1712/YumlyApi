package ru.topbun.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import ru.topbun.utills.Env

fun Application.configureSecurity() {
    authentication {
        jwt {
            realm = Env["JWT_REALM"]
            verifier(
                JWT.require(Algorithm.HMAC256(Env["JWT_SECRET"]))
                    .withAudience(Env["JWT_AUDIENCE"])
                    .withIssuer(Env["JWT_ISSUER"])
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(Env["JWT_AUDIENCE"])) {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { _, _ -> }
        }
    }
}