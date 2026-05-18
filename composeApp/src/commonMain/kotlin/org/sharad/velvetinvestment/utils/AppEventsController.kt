package org.sharad.velvetinvestment.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object AppEventsController {

    private val _appEvent= MutableSharedFlow<AppEvent?>(replay = 1)
    val appEvent=_appEvent.asSharedFlow()


    suspend fun sendEvent(event: AppEvent){
        _appEvent.emit(event)
    }

    suspend fun sendHomeRefreshEvent(){
        _appEvent.emit(AppEvent.HomeEventRefresh)
    }

    suspend fun sendGoalRefreshEvent(){
        _appEvent.emit(AppEvent.GoalEventRefresh)
    }

    suspend fun sendPortfolioRefreshEvent(){
        _appEvent.emit(AppEvent.PortfolioRefreshEvent)
    }

    suspend fun clear() {
        _appEvent.emit(null)
    }

    suspend fun logout(){
        _appEvent.emit(AppEvent.LogOut)
    }

}


sealed interface AppEvent{
    data object HomeEventRefresh: AppEvent
    data object GoalEventRefresh: AppEvent

    data object PortfolioRefreshEvent: AppEvent
    data object LogOut: AppEvent
}