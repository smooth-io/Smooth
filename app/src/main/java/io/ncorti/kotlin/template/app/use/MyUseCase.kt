package io.ncorti.kotlin.template.app.use

import android.util.Log
import io.smooth.use_cases.callback.CallbackUseCase
import io.smooth.use_cases.callback.CallbackWork

class MyUseCase : CallbackUseCase<String, String>() {

    override fun isOneTimeResult(): Boolean = true

    override fun execute(req: String, work: CallbackWork<String, String>): String? {
        Log.i("MyUseCase", "Called: ${req}")
        work.setProgress(mapOf("progress" to 1))
        Thread.sleep(3000)
        work.setProgress(mapOf("progress" to 50))
        Thread.sleep(2000)
        return req
    }

}