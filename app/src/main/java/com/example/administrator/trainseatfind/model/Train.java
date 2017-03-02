package com.example.administrator.trainseatfind.model;

/**
 * Created by Administrator on 2017/2/28.
 */
public class Train {
    private int id;
    private String trainNo;
    private int trainType;
    private String reference;

    public int getId() {return id;}
    public void setId(final int id) {this.id = id;}

    public String getTrainNo() {return trainNo;}
    public void setTrainNo(final String trainNo) {this.trainNo = trainNo;}

    public int getTrainType() {return trainType;}
    public void setTrainType(final int trainType) {this.trainType = trainType;}

    public String getReference() {return reference;}
    public void setReference(final String reference) {this.reference = reference;}

    public String toString() {
        return "id is "+ id + ", traino is " + trainNo + ", reference is " + reference + ", traintype is " + trainType + ".";
    }

}
