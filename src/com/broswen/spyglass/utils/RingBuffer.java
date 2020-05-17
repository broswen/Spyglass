package com.broswen.spyglass.utils;


import java.util.Deque;
import java.util.LinkedList;

public class RingBuffer {

    private Deque<Double> data;
    private int position;
    private int size;
    public RingBuffer(int size){
        data = new LinkedList<>();
        position = 0;
        this.size = size;
    }

    public void add(Double d){
        data.add(d);
        if(data.size() > size)
            data.pop();
    }

    public Double average(){
        return data.stream().mapToDouble(i -> i).average().orElse(0.0);
    }
}
