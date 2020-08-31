package com.jt.jailedtable;

public class AutenticacionComensal {

    protected String mName="";
    protected String mPass="";

    public AutenticacionComensal(String name, String pass){
        mName=name;
        mPass=pass;
    }

    public String getName(){
        return mName;
    }

    public void setName(String name){
        mName=name;
    }

    public String getPass(){
        return mPass;
    }

    public void setPass(String pass){
        mPass=pass;
    }

}