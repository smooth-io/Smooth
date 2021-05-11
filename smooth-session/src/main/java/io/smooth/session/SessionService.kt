package io.smooth.session

import io.smooth.store.Store

class SessionService<SID, S : Session<SID>>(
    private val sessionStore: Store<SID, S>
) {

    //TODO: ON SESSION CHANGED
    //TODO: ON SESSION EXPIRED
    //TODO: ON ALL SESSIONS EXPIRED

    suspend fun save(
        session: S
    ) {
        sessionStore.save(
            Store.SaveDto(session.id, session)
        )
    }

    suspend fun switch(id: SID) {

    }

    suspend fun expire(id: SID) {

    }

    suspend fun renew(id: SID) {

    }


}