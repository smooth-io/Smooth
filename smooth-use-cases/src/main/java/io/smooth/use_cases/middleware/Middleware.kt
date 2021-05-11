package io.smooth.use_cases.middleware

import io.smooth.use_cases.UseCase
import io.smooth.use_cases.UseCaseResult
import kotlin.reflect.KClass

interface Middleware<Req, Res, UC : UseCase<Req, Res>> {

    suspend fun handleResult(useCaseClass: KClass<UC>, result: UseCaseResult<Req, Res>)

}