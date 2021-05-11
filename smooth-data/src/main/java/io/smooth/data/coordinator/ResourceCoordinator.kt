package io.smooth.data.coordinator

import io.smooth.data.source.DataSource


interface ResourceCoordinator<RequestData, Data> {

    val dataChangeStrategy: DataChangeStrategy<RequestData, Data>

    fun areObjectsReferenceMatching(first: Data, second: Data): Boolean

    fun areObjectsTheSame(first: Data, second: Data): Boolean

    fun getLowerSource(source: DataSource<RequestData, Data>): DataSource<RequestData, Data>?

    fun getUpperSource(source: DataSource<RequestData, Data>): DataSource<RequestData, Data>?

}