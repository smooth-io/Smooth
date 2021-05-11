package io.smooth.redux

import io.smooth.use_cases.ExecutionResult
import io.smooth.use_cases.UseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class TestUC : UseCase<String, String>() {

    override suspend fun execute(request: String): Flow<ExecutionResult<String>> = flow {
        success("hi")
        delay(500)
        success("bye")
        delay(500)
        try {
            wee().collect {
                success(it)
            }
        } catch (e: Throwable) {
            error(e)
        }
        delay(500)
        success("welcome again")
    }

    suspend fun wee() = flow<String> {
        emit("wee")
        throw IllegalArgumentException("hahaha")
    }

}