package io.github.ronjunevaldoz.paymongo.util

import io.github.ronjunevaldoz.paymongo.PayMongo.Config
import io.github.ronjunevaldoz.paymongo.exception.PayMongoException
import io.github.ronjunevaldoz.paymongo.models.error.PayMongoErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json

object PayMongoClientFactory {
    private const val API_V1 = "https://api.paymongo.com/v1"

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
            onResponse { response ->

            }
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
                val error: PayMongoErrorResponse = when (cause) {
                    is ClientRequestException -> cause.response.body()
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
}