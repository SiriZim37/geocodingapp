package com.tlt.georepo.manager

import com.tlt.georepo.manager.db.DatabaseManager

object FlowManager {

    const val REGISTER_FLOW = "REGISTER_FLOW"
    const val FORGOTPIN_FLOW = "FORGOTPIN_FLOW"
    const val RESET_PINCODE_FLOW = "RESET_PINCODE_FLOW"
    const val INSURANCE_PAYMENT_FLOW = "INSURANCE_PAYMENT_FLOW"
    const val TAX_PAYMENT_FLOW = "TAX_PAYMENT_FLOW"
    const val INSTALLMENT_PAYMENT_FLOW = "INSTALLMENT_PAYMENT_FLOW"

    fun isRegisterFlow() = DatabaseManager
            .getInstance()
            .getFlowState() == REGISTER_FLOW

    fun isForgotPinFlow() = DatabaseManager
            .getInstance()
            .getFlowState() == FORGOTPIN_FLOW

    fun isResetPinFlow() = DatabaseManager
            .getInstance()
            .getFlowState() == RESET_PINCODE_FLOW

    fun changeToRegisterFlow() {
        DatabaseManager.getInstance().saveFlowState(REGISTER_FLOW)
    }

    fun changeToForgotPinFlow() {
        DatabaseManager.getInstance().saveFlowState(FORGOTPIN_FLOW)
    }

    fun changeToResetPinFlow() {
        DatabaseManager.getInstance().saveFlowState(RESET_PINCODE_FLOW)
    }
}