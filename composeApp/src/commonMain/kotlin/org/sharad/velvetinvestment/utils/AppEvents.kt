package org.sharad.velvetinvestment.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppEvents {

    private val _refreshEvent= MutableSharedFlow<RefreshEvents>(replay = 1)
    val refreshEvents=_refreshEvent.asSharedFlow()

    suspend fun sendEvent(event: RefreshEvents){
        _refreshEvent.emit(event)
    }

}


sealed interface RefreshEvents{
    data object HomeEventRefresh: RefreshEvents
}