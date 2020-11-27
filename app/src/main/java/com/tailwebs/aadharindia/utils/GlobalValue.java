package com.tailwebs.aadharindia.utils;


import android.net.Uri;

import com.tailwebs.aadharindia.center.searchinmap.models.CenterModel;
import com.tailwebs.aadharindia.center.searchinmap.models.CityCenterImage;
import com.tailwebs.aadharindia.center.searchinmap.models.CityCenterImages;
import com.tailwebs.aadharindia.home.models.PaymentTypeModel;
import com.tailwebs.aadharindia.home.models.TaskModel;
import com.tailwebs.aadharindia.housevisit.models.AgriculturalLandsModel;
import com.tailwebs.aadharindia.housevisit.models.FamilyTypesModel;
import com.tailwebs.aadharindia.housevisit.models.HouseTypesModel;
import com.tailwebs.aadharindia.housevisit.models.IllMembersModel;
import com.tailwebs.aadharindia.housevisit.models.KitchenTypesModel;
import com.tailwebs.aadharindia.housevisit.models.MarriedStatusModel;
import com.tailwebs.aadharindia.housevisit.models.NoOfCattlesModel;
import com.tailwebs.aadharindia.housevisit.models.NoOfRoomsModel;
import com.tailwebs.aadharindia.housevisit.models.PastEventsModel;
import com.tailwebs.aadharindia.housevisit.models.RoofTypesModel;
import com.tailwebs.aadharindia.housevisit.models.SocialParametersModel;
import com.tailwebs.aadharindia.housevisit.models.StayLengthsModel;
import com.tailwebs.aadharindia.housevisit.models.ToiletsModel;
import com.tailwebs.aadharindia.housevisit.models.UpcomingEventsModel;
import com.tailwebs.aadharindia.housevisit.models.WallTypesModel;
import com.tailwebs.aadharindia.models.common.CACDCastesModel;
import com.tailwebs.aadharindia.models.common.CACDLoanAmountsModel;
import com.tailwebs.aadharindia.models.common.CACDLoanReasonsModel;
import com.tailwebs.aadharindia.models.common.CACDLoanTakerRelationsModel;
import com.tailwebs.aadharindia.models.common.CACDLoanTenuresModel;
import com.tailwebs.aadharindia.models.common.CACDMaritalStatusModel;
import com.tailwebs.aadharindia.models.common.CACDRationCardTypesModel;
import com.tailwebs.aadharindia.models.common.CACDReligionsModel;
import com.tailwebs.aadharindia.models.common.CACDSecondaryIDModel;
import com.tailwebs.aadharindia.models.common.CCoARelationModel;
import com.tailwebs.aadharindia.models.common.CIEducationLevelsStatusModel;
import com.tailwebs.aadharindia.models.common.CIEducationStatusModel;
import com.tailwebs.aadharindia.models.common.CIHealthStatusModel;
import com.tailwebs.aadharindia.models.common.CIManagementTypesStatusModel;
import com.tailwebs.aadharindia.utils.custom.multipleimageupload.models.ImageUploadModel;
import com.tailwebs.aadharindia.utils.custom.newmultipleimageupload.model.Image;

import java.util.ArrayList;
import java.util.List;

public class GlobalValue {

    public static String secret = null;
    public static String secret_id = null;
    public static String city_id = null;
    public static String latitude = null;
    public static String longitude = null;


    public static List<CenterModel> centerModelList =null;

//    public static ArrayList<ImageUploadModel> centerImagesForConfirmation =null;
    public static List<Image> centerImagesForConfirmationNew =null;
    public static ArrayList<CityCenterImages> ciTyCenterenterImages =null;


    public static  ArrayList<TaskModel> allTasksModelArrayList =null;


    //for center select

    public static boolean selectedCenter = false;
    public static boolean chooseCenterFromList = false;
    public static boolean isGroupPresent = false;
    public static String cityCenterId = null;
    public static String groupId = null;
    public static String centerId = null;
    public static String centerPerson = null;
    public static String centerAddress= null;

    public static String loanTakerId= null;
    public static String taskId= null;
    public static String collectionId= null;
    public static String loanTakerIdForAnalytics= null;


    public static String placeName= null;
    public static String placeId = null;
    public static String placeAddress = null;


    public static String taskName= null;


    //Applicant Common Datas

    public static ArrayList<CACDLoanAmountsModel> applicantLoanAmountArrayList = null;
    public  static  ArrayList<CACDLoanTenuresModel> applicantLoanTenureArrayList = null;
    public static ArrayList<CACDLoanTakerRelationsModel> applicantLoanTakerRelationsArrayList = null;
    public  static  ArrayList<CACDLoanReasonsModel> applicantLoanReasonsArrayList = null;
    public static ArrayList<CACDMaritalStatusModel> applicantMaritalStatusArrayList = null;
    public  static  ArrayList<CACDReligionsModel> applicantReligionsArrayList = null;
    public  static  ArrayList<CACDCastesModel> applicantCastesArrayList = null;
    public static ArrayList<CACDRationCardTypesModel> applicantRationCardTypesArrayList = null;
    public static ArrayList<CACDSecondaryIDModel> applicantSecondaryIDArrayList = null;
    public static ArrayList<String> applicantGenderList=null;

    public static  ArrayList<CCoARelationModel> relationModels = null;


    public  static  ArrayList<CIHealthStatusModel> applicantHealthArrayList = null;
    public static ArrayList<CIEducationStatusModel> applicantEducationArrayList = null;
    public static ArrayList<CIEducationLevelsStatusModel> applicantEducationLevelArrayList = null;
    public static ArrayList<CIManagementTypesStatusModel> applicantManagementTypesList=null;


    public static int secondaryDocumentImageCount = 0;

    public static int bankStatementImageCount = 0;

    public static int otherLoanCardImageCount = 0;


    public static Uri signatureUri = null;
    public static String signatureUriPath = null;


    //House Visit Common Datas

    public static ArrayList<HouseTypesModel> houseTypesModelArrayList = null;
    public  static  ArrayList<RoofTypesModel> roofTypesModelArrayList = null;
    public static ArrayList<WallTypesModel> wallTypesModelArrayList = null;
    public  static  ArrayList<KitchenTypesModel> kitchenTypesModelArrayList = null;
    public static ArrayList<ToiletsModel> toiletsModelArrayList = null;
    public  static  ArrayList<NoOfRoomsModel> noOfRoomsModelArrayList = null;
    public  static  ArrayList<MarriedStatusModel> marriedStatusModelArrayList = null;
    public static ArrayList<AgriculturalLandsModel> agriculturalLandsModelArrayList = null;
    public static ArrayList<NoOfCattlesModel> cattlesModelArrayList = null;
    public static ArrayList<FamilyTypesModel> familyTypesModelArrayList = null;
    public static ArrayList<IllMembersModel> illMembersModelArrayList = null;
    public static ArrayList<StayLengthsModel> stayLengthsModelArrayList = null;

    public static ArrayList<SocialParametersModel> socialParametersModelArrayList=null;


    public static ArrayList<UpcomingEventsModel> upcomingEventsModelArrayList = null;
    public static ArrayList<PastEventsModel> pastEventsModelArrayList = null;

    public static ArrayList<PaymentTypeModel> paymentTypeModelArrayList = null;


    public static int houseImageCount = 0;



    public  static  boolean isApplicantCustomerDetailsCompleted = false;
    public static boolean isApplicantDocumentCompleted=false;
    public static boolean isApplicantDeclarationCompleted =false;
    public static boolean isHouseVisitDeclarationCompleted =false;
    public static boolean isPostApprovalDeclaeationCompleted=false;








}
