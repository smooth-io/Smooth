package io.smooth.data.store.operations

import io.smooth.data.source.operation.modifiying.SaveOperation
import io.smooth.store.Store

class StoreSaveOperation<Id, Data>(private val store: Store<Id, Data>) :
    SaveOperation<Id, Data> {

    override suspend fun save(requestData: Id, data: Data) {
        store.save(Store.SaveDto(requestData, data))
    }

}

