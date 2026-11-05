package org.sharad.velvetinvestment.features.fireonboarding.domain.repository

import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.AssetsStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.FinanceStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.GoalsStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.InsuranceStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.data.remote.model.LoansStepBodyDto
import org.sharad.velvetinvestment.features.fireonboarding.domain.models.FireStage
import org.sharad.velvetinvestment.shared.domain.models.OnboardingStatusDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

/**
 * The FIRE onboarding step endpoints. Each returns the updated onboarding status; callers still
 * reload `GET /user/` before deciding the next step, so that stays the single source of truth.
 * The FIRE report's edit screens save through the same endpoints.
 */
interface FireOnboardingRepository {

    /** Marks [stage] skipped (`{ "skip": true }`). */
    suspend fun skip(stage: FireStage): NetworkResponse<OnboardingStatusDomain, ErrorDomain>

    suspend fun submitFinance(body: FinanceStepBodyDto): NetworkResponse<OnboardingStatusDomain, ErrorDomain>

    suspend fun submitAssets(body: AssetsStepBodyDto): NetworkResponse<OnboardingStatusDomain, ErrorDomain>

    suspend fun submitLoans(body: LoansStepBodyDto): NetworkResponse<OnboardingStatusDomain, ErrorDomain>

    suspend fun submitInsurance(body: InsuranceStepBodyDto): NetworkResponse<OnboardingStatusDomain, ErrorDomain>

    suspend fun submitGoals(body: GoalsStepBodyDto): NetworkResponse<OnboardingStatusDomain, ErrorDomain>
}
