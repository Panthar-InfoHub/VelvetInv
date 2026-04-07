package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.domain.models.goals.GoalRequest
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkError
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface UserFinance {

    suspend fun getFireReport():
            NetworkResponse<FireReportDomain, ErrorDomain>

    suspend fun getFirePdf():
            NetworkResponse<ByteArray, ErrorDomain>

    suspend fun addChildMarriageGoal(goal: GoalRequest.ChildMarriage):
            NetworkResponse<Unit, ErrorDomain>

    suspend fun addChildEducationGoal(goal: GoalRequest.ChildEducation):
            NetworkResponse<Unit, ErrorDomain>

    suspend fun addRetirementGoal(goal: GoalRequest.Retirement):
            NetworkResponse<Unit, ErrorDomain>

    suspend fun addWealthBuildingGoal(goal: GoalRequest.WealthBuildingGoal):
            NetworkResponse<Unit, ErrorDomain>

}