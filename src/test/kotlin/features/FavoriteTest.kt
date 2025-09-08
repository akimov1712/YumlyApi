package features

import appClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.Test
import ru.topbun.features.recipe.entity.GetRecipeReceive
import ru.topbun.module
import kotlin.random.Random
import kotlin.test.assertEquals

class FavoriteTest {

    @Test
    fun switchFavorite()  = testApplication {
        application {
            module()
        }

        val randomId = Random.nextInt(10000)

        appClient.post("/favorite/$randomId").apply {
            assertEquals(HttpStatusCode.OK, status)
        }

    }

    @Test
    fun getFavoriteRecipe()  = testApplication {
        application {
            module()
        }

        val receive = GetRecipeReceive()

        appClient.post("/favorite"){
            setBody(receive)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }

    }


}