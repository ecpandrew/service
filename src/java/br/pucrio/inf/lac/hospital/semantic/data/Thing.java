/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pucrio.inf.lac.hospital.semantic.data;

import java.util.UUID;

/**
 *
 * @author dsc_7
 */
public class Thing extends Device{
    public Thing(UUID thingID, String manufacturer) {
        super(thingID, manufacturer);
    }
}
