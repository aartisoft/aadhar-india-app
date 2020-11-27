package com.tailwebs.aadharindia.postapproval.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.UploadImages;
import com.tailwebs.aadharindia.models.PDFFiles;

import java.util.ArrayList;

public class GroupPostApprovalDocumentModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("group_photo_not_available")
    @Expose
    private String group_photo_not_available;

    @SerializedName("nach_photo_not_available")
    @Expose
    private String nach_photo_not_available;

    @SerializedName("group_post_approval_other_document_not_available")
    @Expose
    private String group_post_approval_other_document_not_available;

    @SerializedName("group_photo")
    @Expose
    private UploadImages group_photo;

    @SerializedName("nach_photo")
    @Expose
    private UploadImages nach_photo;

    @SerializedName("group_post_approval_other_documents")
    @Expose
    private ArrayList<PostApprovalOtherDocumentsModel> postApprovalOtherDocumentsModelArrayList;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_photo_not_available() {
        return group_photo_not_available;
    }

    public void setGroup_photo_not_available(String group_photo_not_available) {
        this.group_photo_not_available = group_photo_not_available;
    }

    public String getNach_photo_not_available() {
        return nach_photo_not_available;
    }

    public void setNach_photo_not_available(String nach_photo_not_available) {
        this.nach_photo_not_available = nach_photo_not_available;
    }

    public String getGroup_post_approval_other_document_not_available() {
        return group_post_approval_other_document_not_available;
    }

    public void setGroup_post_approval_other_document_not_available(String group_post_approval_other_document_not_available) {
        this.group_post_approval_other_document_not_available = group_post_approval_other_document_not_available;
    }

    public UploadImages getGroup_photo() {
        return group_photo;
    }

    public void setGroup_photo(UploadImages group_photo) {
        this.group_photo = group_photo;
    }

    public UploadImages getNach_photo() {
        return nach_photo;
    }

    public void setNach_photo(UploadImages nach_photo) {
        this.nach_photo = nach_photo;
    }

    public ArrayList<PostApprovalOtherDocumentsModel> getPostApprovalOtherDocumentsModelArrayList() {
        return postApprovalOtherDocumentsModelArrayList;
    }

    public void setPostApprovalOtherDocumentsModelArrayList(ArrayList<PostApprovalOtherDocumentsModel> postApprovalOtherDocumentsModelArrayList) {
        this.postApprovalOtherDocumentsModelArrayList = postApprovalOtherDocumentsModelArrayList;
    }
}
