package org.sharad.velvetinvestment.presentation.kyc.uistate

enum class Gender(
    val code: String,
    val displayName: String
) {
    MALE("M", "Male"),
    FEMALE("F", "Female")
}

enum class MaritalStatus(
    val code: String,
    val displayName: String,
) {
    MARRIED("MARRIED", "Married"),
    UNMARRIED("UNMARRIED", "Unmarried")
}

enum class OccupationType(
    val code: String,
    val displayName: String
) {
    BUSINESS("01", "Business"),
    SERVICES("02", "Services"),
    PROFESSIONAL("03", "Professional"),
    AGRICULTURE("04", "Agriculture"),
    RETIRED("05", "Retired"),
    HOUSEWIFE("06", "Housewife"),
    STUDENT("07", "Student"),
    OTHERS("08", "Others");

}