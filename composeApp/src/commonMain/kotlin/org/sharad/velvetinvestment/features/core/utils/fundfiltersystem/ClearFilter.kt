package org.sharad.velvetinvestment.features.core.utils.fundfiltersystem

fun clearFilters(currentFilter: InvestmentFilter): InvestmentFilter {
    val clearedGroups = currentFilter.groups.map { group ->
        group.copy(
            options = group.options.map {
                it.copy(isSelected = false)
            }
        )
    }

    return currentFilter.copy(groups = clearedGroups)
}
