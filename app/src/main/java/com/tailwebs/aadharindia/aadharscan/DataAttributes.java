package com.tailwebs.aadharindia.aadharscan;

/**
 * Created by RajinderPal on 6/22/2016.
 */
public class DataAttributes {
    // declare xml attributes of aadhar card QR code xml response
    public static final String AADHAAR_DATA_TAG = "PrintLetterBarcodeData",
            AADHAR_UID_ATTR = "uid",
            AADHAR_NAME_ATTR = "name",
            AADHAR_GNAME_ATTR = "gname",
            AADHAR_GENDER_ATTR = "gender",
            AADHAR_YOB_ATTR = "yob",
            AADHAR_DOB_ATTR ="dob",
            AADHAR_DOB_GUESS_ATTR ="dobGuess",
            AADHAR_CO_ATTR = "co",
            AADHAR_LM_ATTR ="lm",
            AADHAR_HOUSE_ATTR ="house",
            AADHAR_STREET_ATTR ="street",
            AADHAR_LOC_ATTR ="loc",
            AADHAR_VTC_ATTR = "vtc",
            AADHAR_PO_ATTR = "po",
            AADHAR_DIST_ATTR = "dist",
            AADHAR_SUB_DIST_ATTR = "subdist",
            AADHAR_STATE_ATTR = "state",
            AADHAR_PC_ATTR = "pc";
}

//<?xml version="1.0" encoding="UTF-8"?> <PrintLetterBarcodeData uid="728164930146" name="Siddharth Kajaria" gender="M" yob="1988" co="S/O: Anand Kumar Kajaria" house="Kajaria House" street="157, Chittaranjan Avenue" vtc="Barabazar" po="Barabazar" dist="Kolkata" subdist="Circus Avenue" state="West Bengal" pc="700007" dob="26/09/1988"/>
//<?xml version="1.0" encoding="UTF-8"?> <PrintLetterBarcodeData uid="943804659251" name="Satheeshkumar P" gender="M" yob="1992" gname="Amutha" co="S/O: Perumal" house="973" street="KOLLAKOTTAI" loc="GANDHI NAGAR" vtc="Paramanandal" po="Paramanandal" dist="Tiruvannamalai" subdist="Chengam" state="Tamil Nadu" pc="606710" dob="02/08/1992"/>