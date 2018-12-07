package com.example.erik.androidlabb3;

public class AccuracyFilter {

    private double previousValue;
    private double filterFactor;
    private boolean initial;

    public AccuracyFilter(double filterFactor){
        reset();
        this.filterFactor = filterFactor;
    }

    public double calculateValue(double currentValue){
        if(initial){
            previousValue = currentValue;
            initial = false;
        }
        double filteredValue = filterFactor * previousValue + (1 - filterFactor) * currentValue;
        previousValue = filteredValue;
        return filteredValue;
    }

    public void reset(){
        previousValue = 0;
        initial = true;
    }
}
