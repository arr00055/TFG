package com.jt.jailedtable;

public class creausuariocomensal {

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

    public creausuariocomensal(String name, String apellidos, String numtlf, String contacto, String valorpositivo, String valornegativo, String comunidad, String provincia, String localidad, String password){
        mNombre = name;
        mApellidos = apellidos;
        mNumTlf = numtlf;
        mContacto = contacto;
        mValorPositivo = valorpositivo;
        mValorNegativa = valornegativo;
        mComunidad = comunidad;
        mProvincia = provincia;
        mLocalidad = localidad;
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

    public String getValorPositivo(){
        return mValorPositivo;
    }

    public void setValorPositivo(String valorpositivo){
        mValorPositivo=valorpositivo;
    }

    public String getValorNegativo(){
        return mValorNegativa;
    }

    public void setValorNegativo(String valornegativo){
        mValorNegativa=valornegativo;
    }

    public String getComunidad(){
        return mComunidad;
    }

    public void setComunidad(String comunidad){
        mComunidad=comunidad;
    }

    public String getProvincia(){
        return mProvincia;
    }

    public void setProvincia(String provincia){
        mProvincia=provincia;
    }

    public String getLocalidad(){
        return mLocalidad;
    }

    public void setLocalidad(String localidad){
        mLocalidad=localidad;
    }

    public String getPassword(){
        return mPassword;
    }

    public void setPassword(String password){
        mPassword=password;
    }

}
