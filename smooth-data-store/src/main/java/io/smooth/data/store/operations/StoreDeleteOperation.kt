package io.smooth.data.store.operations

import io.smooth.data.source.operation.modifiying.DeleteOperation
import io.smooth.store.Store

class StoreDeleteOperation<Id, Data>(private val store: Store<Id, Data>) :
    DeleteOperation<Id, Data> {

    override suspend fun delete(requestData: Id, data: Data?): Data? {
        store.deleteById(requestData)
        return data
    }

}

