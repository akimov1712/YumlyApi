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
                VerificationType.SIGN_UP_CONFIRM -> "Ваш код подтверждения"
                VerificationType.RESET_PASSWORD -> "Код для сброса пароля"
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
                      background-color: #f4f4f4;
                      color: #333333;
                      padding: 30px;
                    }
                    .wrapper {
                      max-width: 600px;
                      margin: auto;
                      background: #ffffff;
                      border-radius: 10px;
                      padding: 30px 20px;
                      text-align: center;
                      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
                    }
                    h1 {
                      font-size: 22px;
                      margin-bottom: 20px;
                      font-weight: 600;
                      color: #111111;
                    }
                    .code {
                      background-color: #1AA060;
                      color: #fff;
                      text-align: center;
                      font-size: 26px;
                      font-weight: bold;
                      letter-spacing: 6px;
                      padding: 8px 16px;
                      border-radius: 8px;
                      display: inline-block;
                      margin: 15px 0px 20px;
                    }
                    .info {
                      font-size: 15px;
                      color: #555555;
                    }
                    .footer {
                      margin-top: 25px;
                      font-size: 12px;
                      color: #888888;
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
                setFrom(InternetAddress(username, "Yumly"))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
                this.subject = subject
                this.sender
                setContent(htmlContent, "text/html; charset=utf-8")
            }

            Transport.send(message)

        } catch (e: MessagingException) {
            throw AppException(HttpStatusCode.BadRequest, ErrorMessage.SEND_MESSAGE)
        }
    }
}
