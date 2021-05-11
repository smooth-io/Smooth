package io.smooth.data.store.operations

import io.smooth.data.source.operation.GetOperation
import io.smooth.store.Store
import kotlinx.coroutines.flow.Flow

class StoreGetOperation<Id, Data>(private val store: Store<Id, Data>) :
    GetOperation<Id, Data> {

    override suspend fun get(requestData: Id): Flow<Data?> =
        store.getById(requestData)


}