package io.smooth.use_cases.android

import io.smooth.store.converter.DataConverterStore

interface HasRequestsStore {

    fun getRequestsStore(): DataConverterStore<String, Any, String>

}