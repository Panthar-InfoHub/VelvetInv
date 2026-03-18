package org.sharad.velvetinvestment.domain

enum class LoanTypes(val displayName: String, val code:String) {
    HOME_LOAN("Home Loan", "home"),
    VEHICLE_LOAN("Vehicle Loan","car"),
    EDUCATION_LOAN("Education Loan","education"),
    PERSONAL_LOAN("Personal Loan", "personal"),
    FAMILY_LOAN("Family Loan", "family"),
    OTHER("Other", "other")
}