package ru.topbun.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ru.topbun.utills.Env

fun Application.configureSecurity() {
    authentication {
        jwt {
            realm = Env["REALM"]
            verifier(
                JWT.require(Algorithm.HMAC256(Env["SECRET"]))
                    .withAudience(Env["AUDIENCE"])
                    .withIssuer(Env["ISSUER"])
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(Env["AUDIENCE"])) {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { _, _ -> }
        }
    }
}