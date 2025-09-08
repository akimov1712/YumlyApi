package features

import appClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.junit.Test
import ru.topbun.features.recipe.entity.AddRecipeReceive
import ru.topbun.features.recipe.entity.GetRecipeReceive
import ru.topbun.features.recipe.entity.IngredientReceive
import ru.topbun.features.recipe.entity.features.recipe.entity.StepReceive
import ru.topbun.models.recipe.RecipeDTO
import ru.topbun.module
import kotlin.test.assertEquals

class RecipeTest {

    @Test
    fun getRecipes() = testApplication{
        application {
            module()
        }
        val receive = GetRecipeReceive()
        appClient.post("/recipe"){
            setBody(receive)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun getRecipeWithId() = testApplication{
        application {
            module()
        }
        appClient.get("/recipe/1").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun addDeleteRecipe() = testApplication{
        application {
            module()
        }

        val receive = AddRecipeReceive(
            title = "test",
            description = null,
            previewUrl = null,
            cookingTime = 60,
            kcal = 1000,
            protein = 10.0,
            fat = 10.0,
            carb = 10.0,
            ingredients = listOf(IngredientReceive("","")),
            steps = listOf(StepReceive("", null)),
            tagIds = emptyList()
        )

        val recipe = appClient.post("/recipe/add"){
            setBody(receive)
        }.body<RecipeDTO>()

        appClient.delete("/recipe/${recipe.id}").apply {
            assertEquals(HttpStatusCode.OK, status)
        }

    }

}