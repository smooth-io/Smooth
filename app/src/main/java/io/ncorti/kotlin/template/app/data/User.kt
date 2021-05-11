package io.ncorti.kotlin.template.app.data

import androidx.room.Entity
import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@Entity
open class User(
    @PrimaryKey
    var id: Int = -1,
    var name: String = ""
): RealmModel