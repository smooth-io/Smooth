package io.smooth.data.store.operations

import io.smooth.data.source.operation.modifiying.UpdateOperation
import io.smooth.store.Store

class StoreUpdateOperation<Id, Data>(private val store: Store<Id, Data>) :
    UpdateOperation<Id, Data> {

    override suspend fun update(requestData: Id, oldData: Data?, newData: Data) {
        store.save(Store.SaveDto(requestData, newData))
    }

}

