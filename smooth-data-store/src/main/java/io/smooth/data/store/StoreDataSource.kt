package io.smooth.data.store

import io.smooth.data.source.DataSource
import io.smooth.data.source.operation.modifiying.DeleteOperation
import io.smooth.data.source.operation.modifiying.SaveOperation
import io.smooth.data.source.operation.modifiying.UpdateOperation
import io.smooth.data.store.operations.StoreDeleteOperation
import io.smooth.data.store.operations.StoreGetOperation
import io.smooth.data.store.operations.StoreSaveOperation
import io.smooth.data.store.operations.StoreUpdateOperation
import io.smooth.store.Store

class StoreDataSource<Id, Data>(
    store: Store<Id, Data>,
    name: String,
    priority: Int
) : DataSource<Id, Data>(
    name, priority,
    StoreGetOperation(store)
) {

    override var saveOperation: SaveOperation<Id, Data>? = StoreSaveOperation(store)
    override var updateOperation: UpdateOperation<Id, Data>? = StoreUpdateOperation(store)
    override var deleteOperation: DeleteOperation<Id, Data>? = StoreDeleteOperation(store)

}