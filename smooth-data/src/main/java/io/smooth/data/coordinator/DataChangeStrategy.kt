package io.smooth.data.coordinator

import io.smooth.data.source.DataSource

interface DataChangeStrategy<RequestData, Data> {

    fun onDataChanged(
        resourceCoordinator: ResourceCoordinator<RequestData, Data>,
        requestData: RequestData,
        currentSource: DataSource<RequestData, Data>,
        currentData: Data?,
        targetSource: DataSource<*, *>,
        targetData: Data
    ): DataChangeResolutionType

}