package io.ncorti.kotlin.template.app.data

import io.realm.Realm
import io.smooth.store.realm.RealmStore

class UserStore(realmInstance: Realm) : RealmStore<Int,User>(realmInstance) {
    override fun getDataClazz(): Class<User> = User::class.java
}