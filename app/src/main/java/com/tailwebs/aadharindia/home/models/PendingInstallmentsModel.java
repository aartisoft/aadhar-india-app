package com.tailwebs.aadharindia.home.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PendingInstallmentsModel {

    @SerializedName("pending_installment")
    @Expose
    private PendingInstallmentModel pendingInstallmentModels;

    public PendingInstallmentModel getPendingInstallmentModels() {
        return pendingInstallmentModels;
    }

    public void setPendingInstallmentModels(PendingInstallmentModel pendingInstallmentModels) {
        this.pendingInstallmentModels = pendingInstallmentModels;
    }
}
