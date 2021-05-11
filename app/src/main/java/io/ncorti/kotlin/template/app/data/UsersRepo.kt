package io.ncorti.kotlin.template.app.data

import io.smooth.data.fetch.FetchResult
import io.smooth.data.fetch.strategy.HighToLowFetchStrategy
import io.smooth.data.fetch.strategy.LowToHighFetchStrategy
import io.smooth.data.fetch.strategy.SpecificFetchStrategy
import io.smooth.data.modify.strategy.HighToLowModifyStrategy
import io.smooth.data.modify.strategy.SpecificModifyStrategy
import kotlinx.coroutines.flow.Flow

class UsersRepo(private val userManager: UserManager) {

    suspend fun getUser(id: Int): Flow<FetchResult<Int, User>> =
        userManager.fetch(id, LowToHighFetchStrategy())

    suspend fun saveUser(user: User) =
        userManager.save(
            user.id,
            user,
            HighToLowModifyStrategy()
        ) {
            recoverOnFail = false
            resumeOnFail = false
        }


    suspend fun saveUser2(user: User) =
        userManager.save(
            user.id,
            user,
            SpecificModifyStrategy(arrayListOf("db"))
        ) {
            recoverOnFail = true
            resumeOnFail = false
        }


}