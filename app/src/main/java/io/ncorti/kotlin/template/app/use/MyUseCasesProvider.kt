package io.ncorti.kotlin.template.app.use

import io.smooth.use_cases.UseCase
import io.smooth.use_cases.android.provider.UseCasesProvider

class MyUseCasesProvider : UseCasesProvider {

    override fun <Req, Res> getUseCase(useCaseClassName: String): UseCase<Req, Res, *> =
        MyUseCase() as UseCase<Req, Res, *>

}