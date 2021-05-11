package io.smooth.use_cases.provider

import io.smooth.use_cases.UseCase
import kotlin.reflect.KClass

interface UseCasesProvider {

    fun <Req, Res, UC : UseCase<Req, Res>> getUseCase(useCaseClass: KClass<UC>): UC?

}