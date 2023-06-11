package com.manleysoftware.michael.discgolfapp.application;

public class MutableInt {

    private int value;

    public MutableInt(int value){
        this.value = value;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void increment(){
        value++;
    }

    public void decrement(){
        value--;
    }

    public void incrementBy(int value){
        this.value += value;
    }

    public  void decrementBy(int value){
        this.value -= value;
    }
}
