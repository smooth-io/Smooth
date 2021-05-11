package io.smooth.data

import io.smooth.data.coordinator.DataChangeResolutionType
import io.smooth.data.coordinator.DataChangeStrategy
import io.smooth.data.coordinator.ResourceCoordinator
import io.smooth.data.fetch.FetchResourceManager
import io.smooth.data.source.DataSource

class UserFetchResourceManager : FetchResourceManager<Int, User>() {
    override fun getSources(): List<DataSource<Int, User>> {
        TODO("Not yet implemented")
    }

    override val dataChangeStrategy: DataChangeStrategy<Int, User> =
        object: DataChangeStrategy<Int,User>{
            override fun onDataChanged(
                resourceCoordinator: ResourceCoordinator<Int, User>,
                requestData: Int,
                currentSource: DataSource<Int, User>,
                currentData: User?,
                targetSource: DataSource<*, *>,
                targetData: User
            ): DataChangeResolutionType =
                DataChangeResolutionType.UPDATE
        }

    override fun areObjectsReferenceMatching(first: User, second: User): Boolean =
        first.id == second.id


}