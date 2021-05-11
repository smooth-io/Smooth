package io.smooth.store.json

import io.smooth.store.converter.DataConverter
import io.smooth.store.converter.DataConverterStore
import io.smooth.store.room.RoomStore

open class JsonRoomStore<Data>(
    jsonModelDao: JsonModelDao,
    dataConverter: DataConverter<Data, JsonModel>,
) : DataConverterStore<Int, Data, JsonModel>(
    dataConverter,
    RoomStore(jsonModelDao)
)
