package com.tailwebs.aadharindia.member.cashincome.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tailwebs.aadharindia.member.models.CalculatedEMIModel;
import com.tailwebs.aadharindia.member.models.CustomerModel;
import com.tailwebs.aadharindia.models.ProfileImages;
import com.tailwebs.aadharindia.models.common.CCoARelationModel;
import com.tailwebs.aadharindia.models.common.CIEducationLevelsStatusModel;
import com.tailwebs.aadharindia.models.common.CIEducationStatusModel;
import com.tailwebs.aadharindia.models.common.CIHealthStatusModel;
import com.tailwebs.aadharindia.models.common.CIManagementTypesStatusModel;

import java.util.ArrayList;

public class FamilyModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("studying_class")
    @Expose
    private String studying_class;

    @SerializedName("relation_id")
    @Expose
    private String relation_id;

    @SerializedName("relation_name")
    @Expose
    private String relation_name;

    @SerializedName("relation")
    @Expose
    private CCoARelationModel relationModel;

    @SerializedName("health_id")
    @Expose
    private int health_id;

    @SerializedName("education_id")
    @Expose
    private int education_id;

    @SerializedName("education_level_id")
    @Expose
    private int education_level_id;

    @SerializedName("management_type_id")
    @Expose
    private int management_type_id;

    @SerializedName("health")
    @Expose
    private CIHealthStatusModel healthStatusModel;

    @SerializedName("education")
    @Expose
    private CIEducationStatusModel educationStatusModel;

    @SerializedName("education_level")
    @Expose
    private CIEducationLevelsStatusModel educationLevelsStatusModel;

    @SerializedName("management_type")
    @Expose
    private CIManagementTypesStatusModel managementTypesStatusModel;

    @SerializedName("jobs")
    @Expose
    private ArrayList<JobModel> jobModelArrayList;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getStudying_class() {
        return studying_class;
    }

    public void setStudying_class(String studying_class) {
        this.studying_class = studying_class;
    }

    public String getRelation_id() {
        return relation_id;
    }

    public void setRelation_id(String relation_id) {
        this.relation_id = relation_id;
    }

    public int getHealth_id() {
        return health_id;
    }

    public void setHealth_id(int health_id) {
        this.health_id = health_id;
    }

    public int getEducation_id() {
        return education_id;
    }

    public void setEducation_id(int education_id) {
        this.education_id = education_id;
    }

    public int getEducation_level_id() {
        return education_level_id;
    }

    public void setEducation_level_id(int education_level_id) {
        this.education_level_id = education_level_id;
    }

    public int getManagement_type_id() {
        return management_type_id;
    }

    public void setManagement_type_id(int management_type_id) {
        this.management_type_id = management_type_id;
    }

    public CCoARelationModel getRelationModel() {
        return relationModel;
    }

    public void setRelationModel(CCoARelationModel relationModel) {
        this.relationModel = relationModel;
    }

    public CIHealthStatusModel getHealthStatusModel() {
        return healthStatusModel;
    }

    public void setHealthStatusModel(CIHealthStatusModel healthStatusModel) {
        this.healthStatusModel = healthStatusModel;
    }

    public CIEducationStatusModel getEducationStatusModel() {
        return educationStatusModel;
    }

    public void setEducationStatusModel(CIEducationStatusModel educationStatusModel) {
        this.educationStatusModel = educationStatusModel;
    }

    public CIEducationLevelsStatusModel getEducationLevelsStatusModel() {
        return educationLevelsStatusModel;
    }

    public void setEducationLevelsStatusModel(CIEducationLevelsStatusModel educationLevelsStatusModel) {
        this.educationLevelsStatusModel = educationLevelsStatusModel;
    }

    public CIManagementTypesStatusModel getManagementTypesStatusModel() {
        return managementTypesStatusModel;
    }

    public void setManagementTypesStatusModel(CIManagementTypesStatusModel managementTypesStatusModel) {
        this.managementTypesStatusModel = managementTypesStatusModel;
    }

    public ArrayList<JobModel> getJobModelArrayList() {
        return jobModelArrayList;
    }

    public void setJobModelArrayList(ArrayList<JobModel> jobModelArrayList) {
        this.jobModelArrayList = jobModelArrayList;
    }

    public String getRelation_name() {
        return relation_name;
    }

    public void setRelation_name(String relation_name) {
        this.relation_name = relation_name;
    }
}
