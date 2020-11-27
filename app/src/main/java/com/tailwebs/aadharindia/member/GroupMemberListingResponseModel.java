package com.tailwebs.aadharindia.member;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.LoanTakerLoanDetailsModel;

public class GroupMemberListingResponseModel {

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("errors")
    @Expose
    private Object errors;

    @SerializedName("notice")
    @Expose
    private String notice;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("group")
    @Expose
    private GroupMemberListingModel groupMemberListingModel;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public GroupMemberListingModel getGroupMemberListingModel() {
        return groupMemberListingModel;
    }

    public void setGroupMemberListingModel(GroupMemberListingModel groupMemberListingModel) {
        this.groupMemberListingModel = groupMemberListingModel;
    }
}
