package io.smooth.use_cases.error

import io.smooth.use_cases.UseCase
import kotlin.reflect.KClass

class UseCaseNotFoundError(val useCaseClass: KClass<UseCase<*,*>>): Throwable("Use case not found") {
}