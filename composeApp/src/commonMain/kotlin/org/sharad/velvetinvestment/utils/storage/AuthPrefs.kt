package org.sharad.velvetinvestment.utils.storage

import org.sharad.velvetinvestment.features.core.domain.models.OnboardingStage

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
        /** The Janta Nivesh KYC onboarding stage (an [OnboardingStage] id). */
        private const val KEY_ONBOARDING_STAGE = "onboarding_stage"

        private const val KEY_USER_PHONE_NUMBER = "user_phone_number"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_MPIN_ENABLED = "mpin_enabled"
        private const val KEY_MPIN_SETUP = "mpin_setup"
        private const val KEY_BIOMETRIC_LOGIN_ENABLED = "biometric_login_enabled"

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

    /** The server's KYC onboarding stage, written by login and by each step that succeeds. */
    fun setOnboardingStage(stage: String) {
        prefs.setString(KEY_ONBOARDING_STAGE, stage)
    }

    fun getOnboardingStage(): String? {
        return prefs.getString(KEY_ONBOARDING_STAGE)
    }

    /**
     * Records [stage] as reached, but never moves the stored stage backwards: a step re-done after
     * a later one succeeded must not send a relaunch back to it. (Ported from Janta Nivesh.)
     */
    fun advanceOnboardingStage(stage: OnboardingStage) {
        val stages = OnboardingStage.entries
        val current = getOnboardingStage()?.let { OnboardingStage.fromId(it) }
        if (current != null && stages.indexOf(stage) < stages.indexOf(current)) return
        setOnboardingStage(stage.id)
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

    fun setFullName(name: String) { prefs.setString("user_full_name", name) }
    fun getFullName(): String? = prefs.getString("user_full_name")
    fun setDob(dob: String) { prefs.setString("user_dob", dob) }
    fun getDob(): String? = prefs.getString("user_dob")
    fun setEmail(email: String) { prefs.setString("user_email", email) }
    fun getEmail(): String? = prefs.getString("user_email")
    fun setEmailVerified(verified: Boolean) { prefs.setBoolean("user_email_verified", verified) }
    fun isEmailVerified(): Boolean = prefs.getBoolean("user_email_verified") ?: false

    /** Server's `mpin_enabled`: ask for the PIN on every cold start (JN). */
    fun setMpinEnabled(enabled: Boolean) { prefs.setBoolean(KEY_MPIN_ENABLED, enabled) }
    fun isMpinEnabled(): Boolean = prefs.getBoolean(KEY_MPIN_ENABLED) ?: false

    /** Server's `mpin_is_setup`: a PIN exists, so changing it needs the current one first (JN). */
    fun setMpinSetup(setup: Boolean) { prefs.setBoolean(KEY_MPIN_SETUP, setup) }
    fun isMpinSetup(): Boolean = prefs.getBoolean(KEY_MPIN_SETUP) ?: false

    /**
     * Whether the user has left the biometric shortcut switched on for the app lock. Defaults to
     * on, so a user who has a PIN gets the shortcut until they turn it off; the Biometric Login
     * screen is the only thing that writes it (JN).
     */
    fun setBiometricLoginEnabled(enabled: Boolean) { prefs.setBoolean(KEY_BIOMETRIC_LOGIN_ENABLED, enabled) }
    fun isBiometricLoginEnabled(): Boolean = prefs.getBoolean(KEY_BIOMETRIC_LOGIN_ENABLED) ?: true

    fun clearAuth() {
        // The next person to sign in on this device makes their own choice about biometrics.
        prefs.remove(KEY_BIOMETRIC_LOGIN_ENABLED)
        // The next person to sign in on this device must not inherit this user's app lock.
        prefs.remove(KEY_MPIN_ENABLED)
        prefs.remove(KEY_MPIN_SETUP)
        prefs.remove(KEY_BEARER_TOKEN)
        prefs.remove(KEY_REFRESH_TOKEN)
        prefs.remove(KEY_USER_ID)
        prefs.remove(KEY_LOGIN_STATUS)
        prefs.remove(KEY_ONBOARDING_COMPLETED)
        prefs.remove(KEY_ONBOARDING_STEP)
        prefs.remove(KEY_ONBOARDING_STAGE)
        prefs.remove(KEY_USER_PHONE_NUMBER)
    }
}