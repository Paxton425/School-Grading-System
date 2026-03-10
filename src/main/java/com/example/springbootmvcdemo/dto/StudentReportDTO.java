package com.example.springbootmvcdemo.dto;

import com.example.springbootmvcdemo.model.Result.Term;
import com.example.springbootmvcdemo.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

public class StudentReportDTO {
    private UUID studentId;
    private String firstName;
    public String lastName;
    private double overallTotal;
    private double overallAverage;
    private double highestMark;
    private double lowestMark;
    private HashMap<Term, Double> totalsPerTerm = new HashMap<>();
    private HashMap<Term, Double> averagePerTerm = new HashMap<>();
    private TreeMap<String, TreeMap<Term, TermResult>> results;

   public static class TermResult {
       public double finalGrade;
       public double average;
       public Integer gradeLevel;

       public TermResult( double finalGrade, double average, Integer gradeLevel) {
           this.finalGrade = finalGrade;
           this.average = average;
           this.gradeLevel = gradeLevel;
       }
   }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getOverallAverage() {
        return overallAverage;
    }

    public double getOverallTotal() {
        return overallTotal;
    }

    public void setOverallTotal(double overallTotal) {
        this.overallTotal = overallTotal;
    }

    public void setOverallAverage(double overallAverage) {
        this.overallAverage = overallAverage;
    }

    public double getHighestMark() {
        return highestMark;
    }

    public void setHighestMark(double highestMark) {
        this.highestMark = highestMark;
    }

    public double getLowestMark() {
        return lowestMark;
    }

    public void setLowestMark(double lowestMark) {
        this.lowestMark = lowestMark;
    }

    public TreeMap<String, TreeMap<Result.Term, TermResult>> getResults() {
        return results;
    }

    public HashMap<Term, Double> getTotalsPerTerm() {
        return totalsPerTerm;
    }

    public void setTotalsPerTerm(HashMap<Term, Double> totalsPerTerm) {
        this.totalsPerTerm = totalsPerTerm;
    }

    public HashMap<Term, Double> getAveragePerTerm() {
        return averagePerTerm;
    }

    public void setAveragePerTerm(HashMap<Term, Double> averagePerTerm) {
        this.averagePerTerm = averagePerTerm;
    }

    public void setResults(TreeMap<String, TreeMap<Result.Term, TermResult>> results) {
        this.results = results;
    }
}
