package org.sharad.velvetinvestment.presentation.goals.uimodels

import org.sharad.velvetinvestment.data.remote.model.goalmapping.GoalMapBodyDto
import org.sharad.velvetinvestment.data.remote.model.goalmapping.MapData
import org.sharad.velvetinvestment.presentation.goals.viewmodel.SelectableSchemeUiModel

fun List<SelectableSchemeUiModel>.toBody(goalId: Int): GoalMapBodyDto{
    return GoalMapBodyDto(
        goal_id = goalId,
        map_data = this.map {
            MapData(
                folio = it.folio,
                scheme_id = it.schemeId.toString()
            )
        }
    )
}