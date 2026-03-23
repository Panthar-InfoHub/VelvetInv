package org.sharad.velvetinvestment.utils.storage

class AuthPrefs(
    private val prefs: SharedPreference
) {

    companion object {
        private const val KEY_BEARER_TOKEN = "auth_bearer_token"
        private const val KEY_REFRESH_TOKEN = "auth_refresh_token"
        private const val KEY_USER_ID = "auth_user_id"
        private const val KEY_LOGIN_STATUS = "auth_login_status"

        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_ONBOARDING_STEP = "onboarding_step"

        private const val KEY_USER_PHONE_NUMBER = "user_phone_number"
        private const val KEY_FIRST_LAUNCH = "first_launch"

    }

    fun setBearerToken(token: String) {
        prefs.setString(KEY_BEARER_TOKEN, token)
    }

    fun getBearerToken(): String? {
        return prefs.getString(KEY_BEARER_TOKEN)
    }

    fun setRefreshToken(token: String) {
        prefs.setString(KEY_REFRESH_TOKEN, token)
    }

    fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH_TOKEN)
    }

    fun setUserId(userId: String) {
        prefs.setString(KEY_USER_ID, userId)
    }

    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        prefs.setBoolean(KEY_LOGIN_STATUS, isLoggedIn)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_LOGIN_STATUS) ?: false
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.setBoolean(KEY_ONBOARDING_COMPLETED, completed)
    }

    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED) ?: false
    }

    fun setOnboardingStep(step: Int) {
        prefs.setInt(KEY_ONBOARDING_STEP, step)
    }

    fun getOnboardingStep(): Int {
        return prefs.getInt(KEY_ONBOARDING_STEP) ?: 0
    }

    fun setPhoneNumber(phoneNumber: String) {
        prefs.setString(KEY_USER_PHONE_NUMBER, phoneNumber)
    }

    fun getPhoneNumber(): String? {
        return prefs.getString(KEY_USER_PHONE_NUMBER)
    }

    fun setFirstLaunch(firstLaunch: Boolean) {
        prefs.setBoolean(KEY_FIRST_LAUNCH, firstLaunch)
    }

    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(KEY_FIRST_LAUNCH) ?: true
    }

    fun clearAuth() {
        prefs.remove(KEY_BEARER_TOKEN)
        prefs.remove(KEY_REFRESH_TOKEN)
        prefs.remove(KEY_USER_ID)
        prefs.remove(KEY_LOGIN_STATUS)
        prefs.remove(KEY_ONBOARDING_COMPLETED)
        prefs.remove(KEY_ONBOARDING_STEP)
        prefs.remove(KEY_USER_PHONE_NUMBER)
    }
}