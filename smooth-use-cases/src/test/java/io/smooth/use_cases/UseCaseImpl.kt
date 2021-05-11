package io.smooth.use_cases

import io.smooth.use_cases.callback.CallbackUseCase
import io.smooth.use_cases.callback.CallbackWork

internal class UseCaseImpl : CallbackUseCase<Int?, Int>() {

    override fun isOneTimeResult(): Boolean = true

    override fun execute(
        req: Int?,
        work: CallbackWork<Int?, Int>
    ): Int? = req


}
