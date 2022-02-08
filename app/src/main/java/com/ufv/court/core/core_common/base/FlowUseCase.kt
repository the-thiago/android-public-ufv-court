package com.ufv.court.core.core_common.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform

abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(parameters: P): Flow<Result<R>> = execute(parameters)
        .transform<R, Result<R>> { emit(Result.Success(it)) }
        .catch { e -> emit(Result.Error(e)) }
        .flowOn(coroutineDispatcher)

    protected abstract suspend fun execute(parameters: P): Flow<R>
}
