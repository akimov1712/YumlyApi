package ru.topbun.utills

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall
import models.error.ErrorDTO

data class AppException(val code: HttpStatusCode, override val message: String = "") : Exception()

object ErrorMessage {
    const val VERIFICATION_CODE_NOT_FOUND = "Код подтверждения не найден"
    const val INVALID_CODE_EXPIRED = "Код подтверждения истек, запросите код подтверждения заново"
    const val INVALID_CODE = "Код подтверждения не верный. Попробуйте снова"
    const val UNAUTHORIZED = "Пользователь не авторизован"
    const val USER_NOT_FOUND_WITH_EMAIL_PASSWORD = "Пользователь с указанной почтой или паролем не найден"
    const val USER_NOT_FOUND = "Пользователь не найден"
    const val USER_EXISTS = "Пользователь с такой почтой уже зарегистрирован"
    const val CATEGORY_EXISTS = "Категория с таким именем уже существует"
    const val CATEGORY_NOT_FOUND = "Категория с таким id не найдена"
    const val INVALID_EMAIL = "Неверный формат электронной почты"
    const val USERNAME_LENGTH = "Длина псевдонима должна быть не менее 4 символов"
    const val PASSWORD_LENGTH = "Длина пароля должна быть не менее 6 символов"
    const val CATEGORY_LENGTH = "Длина названия категории должна быть не менее 5 символов"
    const val FORBIDDEN = "Отказано в доступе"
    const val BAD_REQUEST = "Произошла ошибка"
    const val PARAMS_INT = "В параметрах укажите число"
    const val RECIPE_NOT_FOUND = "Рецепт с таким id не найден"
    const val PARAMS_ID = "В качестве параметра передай число"
    const val COUNT_INGREDIENTS = "Число инредиентов не может быть меньше 1 и больше 24"
    const val COUNT_STEPS = "Число шагов не может быть меньше 1 и больше 20"
    const val LENGTH_TITLE = "Заголовок не может быть больше 48 символов"
    const val LENGTH_DESCR = "Описание не может быть больше 500 символов"
    const val COUNT_COOKING_TIME = "Время приготовления не может быть больше 10 дней"
    const val COUNT_NUTRIENTS = "Количество нутриентов не может быть больше 1000"
    const val INVALID_IMAGE_URL = "В качестве картинки укажите URL"
    const val FILE_SIZE_8_MB = "Размер файла превышает ограничение в 8 МБ"
    const val NO_SUPPORT_FORMAT_FILE = "Не поддерживаемый тип файла"
    const val INVALID_IMAGE_FILE = "Недопустимый файл изображения"
    const val NO_FILE_UPLOADED = "Не удалось загрузить файл"
    const val DELETE_RECIPE = "Вы можете удалять только свои рецепты"
}

suspend fun RoutingCall.createError(code: HttpStatusCode, errorMessage: String = "") =
    respond(code, ErrorDTO(code.value, errorMessage))

suspend fun RoutingCall.wrapperException(catch: () -> Unit = {}, finally: () -> Unit = {}, block: suspend () -> Unit){
    try {
        block()
    } catch (e: AppException){
        catch()
        createError(code = e.code, errorMessage = e.message)
    } finally {
        finally()
    }
}