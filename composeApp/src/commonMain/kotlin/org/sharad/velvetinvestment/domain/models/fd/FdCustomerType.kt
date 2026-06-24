package org.sharad.velvetinvestment.domain.models.fd

enum class FdCustomerType(
    val displayName: String
) {
    STANDARD("Standard"),
    SENIOR_CITIZEN("Senior Citizen"),
    FEMALE("Female"),
    SENIOR_CITIZEN_FEMALE("Senior Citizen Female");

    companion object {

        fun from(value: String?): FdCustomerType {
            return entries.firstOrNull {
                it.name.equals(value, ignoreCase = true)
            } ?: STANDARD
        }
    }
}