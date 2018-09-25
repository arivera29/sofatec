/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.are.sofatec;

/**
 *
 * @author aimerrivera
 */
public class UtilDate {

    public static String mesToText(int mes) {
        String s="";
        switch (mes) {
            case 1:
                s = "Enero";
                break;
            case 2:
                s = "Febrero";
                break;
            case 3:
                s = "Marzo";
                break;
            case 4:
                s = "Abril";
                break;
            case 5:
                s = "Mayo";
                break;
            case 6:
                s = "Junio";
                break;
            case 7:
                s = "Julio";
                break;
            case 8:
                s = "Agosto";
                break;
            case 9:
                s = "Septiembre";
                break;
            case 10:
                s = "Octubre";
                break;
            case 11:
                s = "Noviembre";
                break;
            case 12:
                s = "Diciembre";
                break;
            default:
                s = "Desconocido";

        }
        return s;
    }

}
