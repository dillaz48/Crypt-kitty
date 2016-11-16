package com.example.lind42431.unhackableproject;

/**
 * Created by lind42431 on 11/16/2016.
 */

public class Network_Info {
    private static String mainCAP;
    private static String mainSSID;
    private static String mainBSSID;

    public void setMainSSID(String newMainSSID){

        mainSSID = newMainSSID;

    }

    public void setMainBSSID(String newMainBSSID){

        mainBSSID = newMainBSSID;

    }

    public void setMainCAP(String newMainCAP){

        mainCAP = newMainCAP;

    }

    public String getMainSSID(){

        return mainSSID;

    }
    public String getMainBSSID(){

        return mainBSSID;

    }
    public String getMainCAP(){

        return mainCAP;

    }



}
