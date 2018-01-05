/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataObjects;

import java.util.ArrayList;

/**
 *
 * @author Abhi
 */
public class ReportSkeleton {
    private ArrayList<String> cols = new ArrayList<String>();
    
    public ReportSkeleton(String... args){
        for(String arg:args){
            cols.add(arg);
        }
    }   
    
    public ArrayList<String> getCols(){
        return cols;
    }
}
