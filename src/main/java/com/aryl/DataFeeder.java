package com.aryl;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFeeder {

    private Connection connection;
    private String database;
    private String username;
    private String password;


    DataFeeder(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/webcrawler", "root", "");
            System.out.println("Database is connected.");
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
        }
    }

    private void insertValue(String tableName, String[] values){
        try {
            StringBuilder completeVal = new StringBuilder("");
            for(String value : values){
                completeVal.append(value).append(", ");
            }
            completeVal.deleteCharAt(completeVal.length()-2);

            String v = completeVal.toString();

            Statement statement = connection.createStatement();

            String query = "insert into "+tableName+" values("+v+")";

            statement.executeUpdate(query);
            System.out.println("Query successfully fired.");
        } catch (SQLException e) {
            System.out.println("Query failed to executed.");
            e.printStackTrace();
        }
    }

    private ResultSet selectValue(String tableName, int id){
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            String query = "SELECT * FROM "+ tableName + " WHERE id="+id;
            System.out.println(query);
            resultSet = statement.executeQuery(query);
//            System.out.println(resultSet.getString(0));

        } catch (SQLException ex) {
            System.out.println("Query failed to executed.");
            ex.printStackTrace();
        }
        return resultSet;
    }

    private void updateCurrent(int id, String title, int isVisited) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        try {
            Statement statement = connection.createStatement();
            String query = "UPDATE visitingpages SET title = \'" + title + "\', isVisited = " + isVisited + ", firstVisitingTime = \'" + formatter.format(date) + "\', lastVisitingTime = \'" + formatter.format(date) + "\' WHERE id=" + id;
            System.out.println(query);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int start = -1;
    int end = -1;

    public void push(int id, String url, String title, int isVisited){

        insertValue("visitingpages", new String[]{
                String.valueOf(id),
                "\'"+url+"\'",
                "\'"+title+"\'",
                String.valueOf(isVisited),
                "\'NA\'",
                "\'NA\'"
        });
        end++;
    }

    public void update(String title, int isVisited){
        updateCurrent(start, title, isVisited);
        start++;
    }



    public String pop(){
        if (!isEmpty()){
            try {
                ResultSet tempRS = selectValue("visitingpages", start);
                String popItem = null;
                while (tempRS.next()){
                     popItem = tempRS.getString("URL");
                }
                return popItem;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Query failed to executed.");
            }
        }
        return null;
    }


    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public void setStartEnd(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public boolean isEmpty(){
        return (start > end) || (start==-1 && end==-1);
    }


}