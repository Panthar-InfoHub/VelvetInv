package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.initiatemfpurchase.body.Item
import org.sharad.velvetinvestment.data.remote.model.initiatemfpurchase.body.MFInitiatePurchaseBody
import org.sharad.velvetinvestment.domain.models.usercart.SipItemDomain
import org.sharad.velvetinvestment.utils.decrementSlashDateYearYear
import org.sharad.velvetinvestment.utils.incrementSlashDateYearYear
import org.sharad.velvetinvestment.utils.toSlashDate


fun List<SipItemDomain>.toInitiateBodyDto(): MFInitiatePurchaseBody {
    return MFInitiatePurchaseBody(
        items = map { sip ->
            Item(
                amc_code = sip.amcCode,
                prod_code = sip.prodCode,
                sip_freq = sip.sipDetails.frequency,
                sip_amt = sip.sipDetails.sipAmount,
                folio = sip.folio ?: "",
                sip_st_date = sip.sipDetails.startDate.toSlashDate(),
                sip_en_date = sip.sipDetails.endDate.toSlashDate(),
                step_up_required = if (sip.stepUpRequired) "Y" else "N",
                step_up_amount = if (sip.stepUpRequired) sip.stepUpAmount.toString() else "",
                step_up_start_date = if (sip.stepUpRequired) sip.sipDetails.startDate.toSlashDate().incrementSlashDateYearYear() else "",
                step_up_end_date = if (sip.stepUpRequired) sip.sipDetails.endDate.toSlashDate().decrementSlashDateYearYear() else ""
            )
        }
    )
}