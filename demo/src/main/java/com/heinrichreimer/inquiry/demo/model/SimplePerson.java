package com.heinrichreimer.inquiry.demo.model;

import com.heinrichreimer.inquiry.annotations.Column;
import com.heinrichreimer.inquiry.annotations.Table;

import java.util.Random;

@Table("simple_persons")
public class SimplePerson {

    @Column
    private String name;
    @Column
    private Stuff[] stuff;

    public SimplePerson() {
    }

    public SimplePerson(String name) {
        this.name = name;
        Random random = new Random();
        stuff = new Stuff[]{new Stuff(random), new Stuff(random)};
    }

    @Override
    public String toString() {
        return "SimplePerson{" +
                "name='" + name + "'" +
                ", stuff[0]=" + stuff[0].hashCode() +
                ", stuff[1]=" + stuff[1].hashCode() +
                '}';
    }
}