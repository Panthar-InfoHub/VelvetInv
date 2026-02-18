package org.sharad.velvetinvestment.utils.fundfiltersystem

fun onFilterOptionSelected(
    currentFilter: InvestmentFilter,
    groupId: String,
    optionId: String
): InvestmentFilter {

    val updatedGroups = currentFilter.groups.map { group ->

        if (group.id != groupId) return@map group

        val updatedOptions = when (group.selectionType) {

            SelectionType.SINGLE -> {
                group.options.map { option ->
                    option.copy(isSelected = option.id == optionId)
                }
            }

            SelectionType.MULTIPLE -> {
                group.options.map { option ->
                    if (option.id == optionId)
                        option.copy(isSelected = !option.isSelected)
                    else
                        option
                }
            }
        }

        group.copy(options = updatedOptions)
    }

    return currentFilter.copy(groups = updatedGroups)
}
