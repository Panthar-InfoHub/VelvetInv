package org.sharad.velvetinvestment.features.fireonboarding.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.AssetsStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.FinanceStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.FireStepResponseDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.FireStepSkipBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.GoalsStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.InsuranceStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.LoansStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.domain.models.FireStage
import org.sharad.velvetinvestment.features.fireonboarding.domain.repository.FireOnboardingRepository
import org.sharad.velvetinvestment.shared.data.local.mapper.toDomain
import org.sharad.velvetinvestment.shared.domain.models.OnboardingStatusDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest

class FireOnboardingRepositoryImpl(
    private val client: HttpClient
) : FireOnboardingRepository {

    override suspend fun skip(stage: FireStage) = post(stage, FireStepSkipBodyDto())

    override suspend fun submitFinance(body: FinanceStepBodyDto) = post(FireStage.FINANCE, body)

    override suspend fun submitAssets(body: AssetsStepBodyDto) = post(FireStage.ASSETS, body)

    override suspend fun submitLoans(body: LoansStepBodyDto) = post(FireStage.LOAN, body)

    override suspend fun submitInsurance(body: InsuranceStepBodyDto) = post(FireStage.INSURANCE, body)

    override suspend fun submitGoals(body: GoalsStepBodyDto) = post(FireStage.GOALS, body)

    private suspend inline fun <reified T> post(
        stage: FireStage,
        body: T
    ): NetworkResponse<OnboardingStatusDomain, ErrorDomain> {
        val response = safeRequest<FireStepResponseDto> {
            client.post(getUrl(stage.path)) {
                setBody(body)
            }
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(
                response.data.onboarding?.toDomain() ?: OnboardingStatusDomain.EMPTY
            )
        }
    }
}
