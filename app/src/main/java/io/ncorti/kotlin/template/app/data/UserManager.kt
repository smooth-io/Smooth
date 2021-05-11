package io.ncorti.kotlin.template.app.data

import io.realm.Realm
import io.smooth.data.ResourceManager
import io.smooth.data.coordinator.DataChangeResolutionType
import io.smooth.data.coordinator.DataChangeStrategy
import io.smooth.data.coordinator.ResourceCoordinator
import io.smooth.data.source.DataSource
import io.smooth.data.store.StoreDataSource
import io.smooth.store.memory.FIFOInMemoryStore
import io.smooth.store.memory.InMemoryStore
import io.smooth.store.memory.LRUInMemoryStore
import io.smooth.store.memory.PerpetualInMemoryStore
import io.smooth.store.realm.RealmStore

class UserManager() : ResourceManager<Int, User>() {

    private lateinit var realmStore: RealmStore<Int, User>

    override fun preInit() {
        realmStore = object : RealmStore<Int, User>(Realm.getDefaultInstance()) {
            override fun getDataClazz(): Class<User> = User::class.java
        }
    }

    override fun initSources(): List<DataSource<Int, User>> =
        arrayListOf(
            StoreDataSource<Int, User>(realmStore, "db", 3),
            StoreDataSource(
                LRUInMemoryStore<Int, User>(),
                "db22", 2
            ),
            StoreDataSource(
                PerpetualInMemoryStore<Int, User>(),
                "memory", 1
            )
        )

    override val dataChangeStrategy: DataChangeStrategy<Int, User> =
        object : DataChangeStrategy<Int, User> {
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