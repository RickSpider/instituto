/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.instituto.fe.model;

/**
 *
 * @author BlackSpider
 */
public class NotaCreditoDebito {
    
    private Long motivoEmision;
    private String descripcionMotivoEmision;
    public Long getMotivoEmision() {
        return motivoEmision;
    }

    /**
     * 
     * Posibles Valores
     * 
     * 1:Devolucion y Ajuste de precios
     * 2:Devolucion
     * 3:Descuento
     * 4:Bonificacion
     * 5:Credito incobrable
     * 6:Recupero de costo
     * 7:Recupero de gasto
     * 8:Ajuste de precio
     * 
     * 
     * @param motivoEmision 
     */
    public void setMotivoEmision(Long motivoEmision) {
        this.motivoEmision = motivoEmision;
    }

	public String getDescripcionMotivoEmision() {
		return descripcionMotivoEmision;
	}

	public void setDescripcionMotivoEmision(String descripcionMotivoEmision) {
		this.descripcionMotivoEmision = descripcionMotivoEmision;
	}

}
