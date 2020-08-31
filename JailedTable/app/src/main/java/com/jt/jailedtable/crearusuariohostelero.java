package com.jt.jailedtable;

public class crearusuariohostelero {
    protected String mNombre="";
    protected String mApellidos="";
    protected String mNumTlf="";
    protected String mContacto="";
    protected String mValorPositivo="";
    protected String mValorNegativa="";
    protected String mComunidad="";
    protected String mProvincia="";
    protected String mLocalidad="";
    protected String mPassword="";

    public crearusuariohostelero(String name, String apellidos, String numtlf, String contacto, String password){
        mNombre = name;
        mApellidos = apellidos;
        mNumTlf = numtlf;
        mContacto = contacto;
        mPassword = password;
    }

    public String getNombre(){
        return mNombre;
    }

    public void setName(String name){
        mNombre=name;
    }

    public String getApellidos (){
        return mApellidos;
    }

    public void setApellidos(String apellidos){
        mApellidos=apellidos;
    }

    public String getNumTlf(){
        return mNumTlf;
    }

    public void setNumTlf(String numtlf){
        mNumTlf=numtlf;
    }

    public String getContacto(){
        return mContacto;
    }

    public void setContacto(String contacto){
        mContacto=contacto;
    }

    public String getPassword(){
        return mPassword;
    }

    public void setPassword(String password){
        mPassword=password;
    }
}
