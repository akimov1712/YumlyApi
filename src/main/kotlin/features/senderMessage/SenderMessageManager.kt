package features.senderMessage

import io.ktor.http.HttpStatusCode
import jakarta.mail.Authenticator
import jakarta.mail.Message
import jakarta.mail.MessagingException
import jakarta.mail.PasswordAuthentication
import jakarta.mail.Session
import jakarta.mail.Transport
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import models.verification.VerificationType
import ru.topbun.models.verification.VerificationDTO
import ru.topbun.utills.AppException
import ru.topbun.utills.Env
import ru.topbun.utills.ErrorMessage
import ru.topbun.utills.parseToString
import java.util.Properties

object SenderMessageManager {

    private val username = Env["SENDER_EMAIL"]
    private val password = Env["SENDER_APP_PASSWORD"]

    private val props = Properties().apply {
        put("mail.smtp.auth", "true")
        put("mail.smtp.starttls.enable", "true")
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.port", "587")
    }

    private val session = Session.getInstance(props, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(username, password)
        }
    })

    suspend fun sendVerificationMessage(email: String, code: VerificationDTO) {
        try {
            val title = when (code.type) {
                VerificationType.SIGN_UP_CONFIRM -> "Добро пожаловать в Yumly!"
                VerificationType.RESET_PASSWORD -> "Сброс пароля в Yumly"
            }
            val subject = when (code.type) {
                VerificationType.SIGN_UP_CONFIRM -> "Ваш код подтверждения — Yumly"
                VerificationType.RESET_PASSWORD -> "Код для сброса пароля — Yumly"
            }
            val timeExpires = code.expiresAt.parseToString()

            val htmlContent = """
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                  <meta charset="UTF-8" />
                  <style>
                    body {
                      margin: 0;
                      font-family: 'Segoe UI', Roboto, Arial, sans-serif;
                      background-color: #121212;
                      color: #f1f1f1;
                      padding: 0;
                    }
                    .wrapper {
                      padding: 40px 20px;
                      text-align: center;
                    }
                    h1 {
                      font-size: 24px;
                      margin-bottom: 30px;
                      font-weight: 600;
                      color: #ffffff;
                    }
                    .code {
                      background-color: #1FCC79;
                      color: #ffffff;
                      font-size: 40px;
                      font-weight: bold;
                      letter-spacing: 8px;
                      padding: 20px 40px;
                      border-radius: 12px;
                      display: inline-block;
                      margin: 20px 0;
                      box-shadow: 0 6px 20px rgba(31, 204, 121, 0.4);
                    }
                    .info {
                      margin-top: 25px;
                      font-size: 15px;
                      color: #bbbbbb;
                    }
                    .footer {
                      margin-top: 40px;
                      font-size: 12px;
                      color: #666666;
                    }
                  </style>
                </head>
                <body>
                  <div class="wrapper">
                    <h1>$title</h1>
                    <div class="code">${code.code}</div>
                    <div class="info">
                      Код действителен до <b>$timeExpires</b><br/>
                      Никому его не сообщайте.
                    </div>
                    <div class="footer">
                      © ${java.time.Year.now()} Yumly — все права защищены
                    </div>
                  </div>
                </body>
                </html>
            """.trimIndent()

            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(username))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
                this.subject = subject
                setContent(htmlContent, "text/html; charset=utf-8")
            }

            Transport.send(message)

        } catch (e: MessagingException) {
            throw AppException(HttpStatusCode.BadRequest, ErrorMessage.SEND_MESSAGE)
        }
    }
}
