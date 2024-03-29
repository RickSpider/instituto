/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instituto.fe.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author BlackSpider
 */
public class Timbrado {
 
    private String timbrado;
    private String establecimiento;
    private String puntoExpedicion;
    private String documentoNro;
    private Date fecIni;

    public String getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(String timbrado) {
        this.timbrado = timbrado;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getPuntoExpedicion() {
        return puntoExpedicion;
    }

    public void setPuntoExpedicion(String puntoExpedicion) {
        this.puntoExpedicion = puntoExpedicion;
    }

    public String getDocumentoNro() {
        return documentoNro;
    }

    public void setDocumentoNro(String documentoNro) {
        this.documentoNro = documentoNro;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }
    
    public String getFechaIniSDF(){
        return new SimpleDateFormat("dd-MM-yyyy").format(fecIni);
    }
    
    public String getDocNumFull(){
        return this.establecimiento+"-"+this.puntoExpedicion+"-"+this.documentoNro;
    }
    
    
    
}
