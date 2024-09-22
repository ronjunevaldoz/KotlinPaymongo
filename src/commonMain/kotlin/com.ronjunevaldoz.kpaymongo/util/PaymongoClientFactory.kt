package com.ronjunevaldoz.kpaymongo.util

import com.ronjunevaldoz.kpaymongo.Paymongo.Config
import com.ronjunevaldoz.kpaymongo.PaymongoJson
import com.ronjunevaldoz.kpaymongo.exception.PayMongoException
import com.ronjunevaldoz.kpaymongo.models.error.PayMongoErrorResponse
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.serialization.decodeFromString

object PaymongoClientFactory {
    private const val API_V1 = "https://api.paymongo.com/v1"

    @OptIn(InternalAPI::class)
    fun client(
        config: Config
    ): HttpClient = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(UserAgent) {
            agent = config.userAgent
        }
        install(ContentNegotiation) {
            json(config.json)
        }
        install(Auth) {
            basic {
                sendWithoutRequest { true }
                credentials {
                    BasicAuthCredentials(username = config.secretKey, password = "")
                }
            }
        }
        install(ResponseObserver) {
            onResponse { response -> }
        }
        HttpResponseValidator {
            validateResponse { response: HttpResponse ->
                val statusCode = response.status.value
                when (statusCode) {
                    in 300..399 -> throw RedirectResponseException(response, "")
                    in 400..499 -> throw ClientRequestException(response, "")
                    in 500..599 -> throw ServerResponseException(response, "")
                }

                if (statusCode >= 600) {
                    throw ResponseException(response, "")
                }
            }

            handleResponseExceptionWithRequest { cause: Throwable, request: HttpRequest ->
                val error = when (cause) {
                    is ClientRequestException -> getError(cause.response.content)
                    else -> throw cause
                }
                throw PayMongoException(error.errors)
            }
        }
        defaultRequest {
            url(API_V1)
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Accept, "application/json")
        }
    }


    private suspend fun getError(responseContent: ByteReadChannel): PayMongoErrorResponse {
        responseContent.readUTF8Line()?.let {
            return PaymongoJson.decodeFromString(it)
        }
        throw IllegalArgumentException("not a parsable error")
    }
}