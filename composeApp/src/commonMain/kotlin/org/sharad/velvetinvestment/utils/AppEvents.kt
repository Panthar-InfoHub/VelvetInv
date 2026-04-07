package org.sharad.velvetinvestment.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppEvents {

    private val _refreshEvent= MutableSharedFlow<RefreshEvents?>(replay = 1)
    val refreshEvents=_refreshEvent.asSharedFlow()

    suspend fun sendEvent(event: RefreshEvents){
        _refreshEvent.emit(event)
    }

    suspend fun sendHomeRefreshEvent(){
        _refreshEvent.emit(RefreshEvents.HomeEventRefresh)
    }

    suspend fun sendGoalRefreshEvent(){
        _refreshEvent.emit(RefreshEvents.GoalEventRefresh)
    }

    suspend fun clear() {
        _refreshEvent.emit(null)
    }

}


sealed interface RefreshEvents{
    data object HomeEventRefresh: RefreshEvents
    data object GoalEventRefresh: RefreshEvents
}