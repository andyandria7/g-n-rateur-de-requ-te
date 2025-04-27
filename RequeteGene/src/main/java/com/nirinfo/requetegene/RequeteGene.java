/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.nirinfo.requetegene;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

/**
 *
 * @author ANDI
 */
public class RequeteGene {

    public static void main(String[] args) {
        Database db = new Database("ventevoiture", "root", "");
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        
        Principal p = new Principal();
        p.setVisible(true);
        
    }
}
