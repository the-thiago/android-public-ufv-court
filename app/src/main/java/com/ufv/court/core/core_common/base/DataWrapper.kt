package com.ufv.court.core.core_common.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T : Any> requestWrapper(call: suspend () -> T): T =
    try {
        call()
    } catch (exception: Exception) {
        exception.printStackTrace()
        when (exception) {
            is UnknownHostException, is SocketTimeoutException -> throw NoNetworkWithServer()
            is SocketException -> throw NoNetworkException()
            is HttpException -> throw exception.tryToParseError()
            else -> throw exception
        }
    }

fun <T : Any> flowRequestWrapper(call: suspend () -> T): Flow<T> = flow {
    emit(requestWrapper(call))
}

fun HttpException.toRemoteDataException(message: String? = null): RemoteDataException {
    return RemoteDataException(
        code = this.code(),
        messageError = message ?: this.message()
    )
}

fun HttpException.tryToParseError() = try {
    RemoteDataException(code(), response()?.errorBody()?.string() ?: message())
} catch (exception: Exception) {
    toRemoteDataException()
}

class NoNetworkException(message: String? = "No Internet Connection") : Throwable(message)

class NoNetworkWithServer(message: String? = "Connection Lost") : Throwable(message)

class RemoteDataException(val code: Int, val messageError: String = "Erro inesperado") :
    Throwable(messageError)
