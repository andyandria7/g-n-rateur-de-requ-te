/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nirinfo.requetegene;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANDI
 */
public class Database {

    private Connection con;
    private String request;

    public Database(String dbName, String dbUser, String dbPass) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName.trim(), dbUser.trim(), dbPass.trim());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Database insert(String tableName) {
        this.request = "INSERT INTO " + tableName.trim();
        return this;
    }

    public Database fields(String[] champs) {
        String listChamps = "(";
        for (int i = 0; i < champs.length; i++) {
            listChamps = listChamps + "" + champs[i] + ",";
        }
        listChamps = listChamps.substring(0, listChamps.length() - 1);
        this.request += listChamps + ")";

        String trous = "(";
        for (int i = 0; i < champs.length; i++) {
            trous = trous + "?,";
        }
        trous = trous.substring(0, trous.length() - 1);
        this.request += " VALUES " + trous + ")";
        return this;
    }

    public Database values(String[] data) {
        String listeDonnée = "(";
        PreparedStatement pre;
        try {
            pre = con.prepareStatement(this.request);
            int num = 1;
            for (int i = 0; i < data.length; i++) {
                pre.setString(num, data[i]);
                num++;
            }
            pre.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return this;
    }

    public ResultSet getResultSet(String[] params) {

        PreparedStatement pre;
        try {
            pre = con.prepareStatement(this.request);
            for (int i = 0; i < params.length; i++) {
                pre.setString(i + 1, params[i].trim());
            }
            return pre.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
    private String currentTable = "";

    public Database select(String columns) {
        this.request = "SELECT " + columns.trim() + " FROM " + this.currentTable;
        return this;
    }

    public String[][] executeV() {
        String[][] tab = null;
        ResultSet rs = null;
        ResultSet val = null;
        int ligne = 0;
        try {
            Statement st = con.createStatement();
            val = st.executeQuery(this.request);
            int i = 0;
            while (val.next()) {
                i++;
            }
            tab = new String[i][4];
//            rs = st.executeQuery(
//                    "SELECT v.matricule, v.marque, v.couleur, p.nom, v.contactPers "
//                    + "FROM voiture v "
//                    + "JOIN personne p ON v.contactPers = p.contactPers");

            rs = st.executeQuery(this.request);
            while (rs.next()) {
                tab[ligne][0] = rs.getString("matricule");
                tab[ligne][1] = rs.getString("marque");
                tab[ligne][2] = rs.getString("couleur");
                tab[ligne][3] = rs.getString("nom");
                ligne++;
            }
            System.out.println(this.request);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return tab;
    }

    public String[][] execute() {
        String[][] tab = null;
        ResultSet rs = null;
        ResultSet val = null;
        int ligne = 0;
        try {
            Statement st = con.createStatement();
            val = st.executeQuery(this.request);
            int i = 0;

            while (val.next()) {
                i++;
            }

            tab = new String[i][4];
            rs = st.executeQuery(this.request);
            while (rs.next()) {
                tab[ligne][0] = rs.getString("contactPers");
                tab[ligne][1] = rs.getString("nom");
                tab[ligne][2] = rs.getString("prenom");
                tab[ligne][3] = rs.getString("adresse");
                ligne++;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return tab;
    }

    public Database read(String tableName) {
        this.request = "";
        this.request += "SELECT * FROM " + tableName.trim();
        return this;
    }

    public Database update(String tableName) {
        this.request = "";
        this.request = "UPDATE " + tableName;
        return this;
    }
    private String[] setValues;

    public Database set(String[] fields) {
//        this.setValues = values;
        this.request += " SET ";
        for (int i = 0; i < fields.length; i++) {
            this.request += fields[i] + "=?,";
        }

        this.request = this.request.substring(0, this.request.length() - 1);
        return this;
    }

//    public Database where(String column, String value) {
//        this.request += " WHERE " + column + "=?";
//        int num = 1;
//        try {
//            PreparedStatement pre = con.prepareStatement(this.request);
//             for (int i = 0; i < setValues.length; i++) {
//                pre.setString(num, setValues[i]);                
//                num++;
//            }
//             pre.setString(num, value);
//             pre.executeUpdate();
//        } catch (SQLException ex) {
//            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
//        }
//         System.out.println(this.request);
//        return this;
//    }
    public Database delete(String tableName) {
        this.request = "";
        this.request = " DELETE FROM " + tableName.trim();
        return this;
    }
    private String op;

//    AND
//        SELECT * FROM login WHERE email =? AND is_approved = TRUE
    public Database and(String column, String value) {

        this.request += " AND " + column + "?";
        PreparedStatement pre;
        try {
            pre = con.prepareStatement(this.request);
            pre.setString(1, this.op);
            pre.setString(2, value);
            ResultSet rs = pre.executeQuery();
            ResultSetMetaData data = rs.getMetaData();

            while (rs.next()) {
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    String Name = data.getColumnName(i);
                    String Value = rs.getString(i);
                    System.out.print(Name.trim() + ": " + Value.trim() + "");
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return this;
    }

    public Database where(String champs, String operator) {
        this.request += " WHERE " + champs.trim() + operator;
        System.out.println(this.request);
        return this;
    }

//    or
    public Database or(String column, String operator) {
        this.request += " OR " + column.trim() + operator;
        System.out.println(this.request);
        return this;
    }

    public Database join(String joinTable, String onCondition) {
        this.request += " INNER JOIN " + joinTable.trim() + " ON " + onCondition.trim();
        return this;
    }

    public Database leftJoin(String tableName, String condition) {
        this.request += " LEFT JOIN " + tableName.trim() + " ON " + condition.trim();
        return this;
    }

    public Database rightJoin(String tableName, String condition) {
        this.request += " RIGHT JOIN " + tableName.trim() + " ON " + condition.trim();
        return this;
    }

    public Database like(String filtre) {
        this.request += " LIKE '" + filtre + "%'";
        return this;
    }
}
