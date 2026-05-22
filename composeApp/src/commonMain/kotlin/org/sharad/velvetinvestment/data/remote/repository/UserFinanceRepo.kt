package org.sharad.velvetinvestment.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.data.mapper.toDomain
import org.sharad.velvetinvestment.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.data.remote.model.addgoals.ChildGoalBodyDto
import org.sharad.velvetinvestment.data.remote.model.addgoals.RetirementGoalBodyDto
import org.sharad.velvetinvestment.data.remote.model.addgoals.WealthBuildingGoalBodyDto
import org.sharad.velvetinvestment.data.remote.model.casreport.RedirectBody
import org.sharad.velvetinvestment.data.remote.model.fdportfoliobyid.FDPortFolioById
import org.sharad.velvetinvestment.data.remote.model.fdredirect.FDRedirectDto
import org.sharad.velvetinvestment.data.remote.model.firereport.FireReportDto
import org.sharad.velvetinvestment.data.remote.model.goalbyid.GoalByIdDto
import org.sharad.velvetinvestment.data.remote.model.goalmapping.GoalMapBodyDto
import org.sharad.velvetinvestment.data.remote.model.loan.SingleLoanDto
import org.sharad.velvetinvestment.data.remote.model.loan.UpdateLoanRequest
import org.sharad.velvetinvestment.data.remote.model.onboarding.Loan
import org.sharad.velvetinvestment.data.remote.model.investmentratedto.InvestmentRateDto
import org.sharad.velvetinvestment.data.remote.model.pendingorders.PendingOrdersDto
import org.sharad.velvetinvestment.data.remote.model.portfolio.UserPortFolioDto
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomain
import org.sharad.velvetinvestment.domain.models.goals.GoalDomain
import org.sharad.velvetinvestment.domain.models.goals.GoalRequest
import org.sharad.velvetinvestment.domain.models.portfolio.FixedDepositTransactionDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioDomain
import org.sharad.velvetinvestment.domain.models.user.InvestmentRateDomain
import org.sharad.velvetinvestment.data.remote.model.loan.UserLoanDto
import org.sharad.velvetinvestment.data.remote.model.report.ReportExportDto
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.loan.LoanDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PendingOrderDomain
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.ErrorType
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest

class UserFinanceRepo(
    private val client: HttpClient
): UserFinance {

    override suspend fun getFireReport(): NetworkResponse<FireReportDomain, ErrorDomain> {
        val response= safeRequest<FireReportDto> {
            client.get(getUrl("/fire-report")) {
                parameter("include_emi", true)
                parameter("projection_years",20)
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun getFirePdf(): NetworkResponse<ByteArray, ErrorDomain> {
        return safeRequest<ByteArray>{
            client.get(getUrl("/fire-report/pdf")) {
            }
        }
    }

    override suspend fun addChildMarriageGoal(goal: GoalRequest.ChildMarriage): NetworkResponse<Unit, ErrorDomain> {
        val body= ChildGoalBodyDto(
            child_age = goal.childAge,
            child_name = goal.childName,
            current_goal_cost = goal.currentGoalCost,
            current_saved_amount = goal.currentSavedAmount,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            return_rate = goal.returnRate,
            years_left = goal.yearsToGoal
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun addChildEducationGoal(goal: GoalRequest.ChildEducation): NetworkResponse<Unit, ErrorDomain> {
        val body= ChildGoalBodyDto(
            child_age = goal.childAge,
            child_name = goal.childName,
            current_goal_cost = goal.currentGoalCost,
            current_saved_amount = goal.currentSavedAmount,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            return_rate = goal.returnRate,
            years_left = goal.yearsToGoal
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun addRetirementGoal(goal: GoalRequest.Retirement): NetworkResponse<Unit, ErrorDomain> {
        val body= RetirementGoalBodyDto(
            current_age = goal.currentAge,
            current_monthly_expense = goal.currentMonthlyExpense,
            current_saved_amount = goal.currentSavedAmount,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            life_expectancy = goal.lifeExpectancy,
            post_retirement_return = goal.postRetirementReturn.toDouble(),
            retirement_age = goal.retirementAge,
            return_rate = goal.returnRate.toDouble()
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun addWealthBuildingGoal(goal: GoalRequest.WealthBuildingGoal): NetworkResponse<Unit, ErrorDomain> {
        val body= WealthBuildingGoalBodyDto(
            current_goal_cost = goal.currentGoalCost,
            current_saved_amount = goal.currentSavedAmount,
            goal_item_id = goal.goalItemId,
            goal_item_name = goal.goalItemName,
            goal_name = goal.goalName,
            goal_type_id = goal.goalTypeId,
            inflation_rate = goal.inflationRate.toDouble(),
            return_rate = goal.returnRate.toDouble(),
            years_left = goal.yearsToGoal
        )
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal")) {
                setBody(body)
            }
        }
    }

    override suspend fun deleteGoal(goalId: String): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.delete(getUrl("/user-goal/$goalId"))
        }
    }

    override suspend fun getPortfolio(): NetworkResponse<PortfolioDomain, ErrorDomain> {
        val response= safeRequest<UserPortFolioDto> {
            client.get(getUrl("/user/portfolio"))
        }

        return when(response){
            is NetworkResponse.Error->{
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success ->{
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
        }
    }

    override suspend fun getFDPortfolioById(id: String): NetworkResponse<FixedDepositTransactionDomain, ErrorDomain> {
        val response = safeRequest<FDPortFolioById> {
            client.get(getUrl("/fd/transactions/$id"))
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
        }
    }

    override suspend fun getFDRedirectUrl(
        id: String,
        event: String
    ): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<FDRedirectDto> {
            client.post(
                getUrl("/fd/redirect-url")
            ) {
                setBody(
                    RedirectBody(
                        fd_trans_id = id,
                        event = event
                    )
                )
            }
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data.data.redirectionUrl)
            }
        }
    }

    override suspend fun getInvestmentRateData(): NetworkResponse<InvestmentRateDomain, ErrorDomain> {
        val response = safeRequest<InvestmentRateDto> {
            client.get(getUrl("/user/investment-rate"))
        }

        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
        }

    }

    override suspend fun getGoalById(id: String): NetworkResponse<GoalDomain, ErrorDomain> {
        val response = safeRequest <GoalByIdDto>{ client.get(getUrl("/user-goal/$id")) }
        return when(response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.data.toDomain()
                )
            }
        }
    }

    override suspend fun mapGoal(body: GoalMapBodyDto): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.post(getUrl("/user-goal/map")) {
                setBody(body)
            }
        }
    }

    override suspend fun deleteSingleLoan(id: String): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.delete(getUrl("/user-loan/$id"))
        }
    }

    override suspend fun getLoans(
        page: Int,
        limit: Int
    ): NetworkResponse<PaginatedData<LoanDomain>, ErrorDomain> {
        val response = safeRequest<UserLoanDto> {
            client.get(getUrl("/user-loan")) {
                parameter("page", page)
                parameter("limit", limit)
            }
        }

        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                val data = response.data.data
                NetworkResponse.Success(
                    PaginatedData(
                        items = data.loans,
                        page = data.pagination.current_page,
                        pageSize = data.pagination.limit,
                        totalItems = data.pagination.total_loans,
                        totalPages = data.pagination.total_pages,
                        hasNextPage = data.pagination.current_page < data.pagination.total_pages
                    )
                )
            }
        }
    }

    override suspend fun getLoanById(id: String): NetworkResponse<LoanDomain, ErrorDomain> {
        val response = safeRequest<SingleLoanDto> {
            client.get(getUrl("/user-loan/$id"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data)
        }
    }

    override suspend fun addSingleLoan(data: Loan): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.post(getUrl("/user-loan")) {
                setBody(data)
            }
        }
    }

    override suspend fun updateSingleLoan(
        loanId: String,
        data: UpdateLoanRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.patch(getUrl("/user-loan/$loanId")) {
                setBody(data)
            }
        }
    }

    override suspend fun exportReport(
        type: String,
        year: Int?,
        folio: String?,
        expand: Int?
    ): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<ReportExportDto> {
            client.get(getUrl("/report")) {
                parameter("type", type)
                year?.let { parameter("year", it) }
                folio?.let { parameter("folio", it) }
                expand?.let { parameter("expand", it) }
            }
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                val url = response.data.data.result
                if (url==null){
                     NetworkResponse.Error(ErrorDomain(
                        code = -1,
                        message = "No Data Found",
                        type = ErrorType.UNKNOWN
                    ))
                }
                else
                NetworkResponse.Success(url)
            }
        }
    }

    override suspend fun getPendingOrders(): NetworkResponse<List<PendingOrderDomain>, ErrorDomain> {
        val response = safeRequest<PendingOrdersDto> {
            client.get(getUrl("/user/pending-orders"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.data?.pending_orders?.map { it.toDomain() } ?: emptyList()
                )
            }
        }
    }
}