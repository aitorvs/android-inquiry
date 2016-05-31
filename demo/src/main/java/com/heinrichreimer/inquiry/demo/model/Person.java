package com.heinrichreimer.inquiry.demo.model;

import com.heinrichreimer.inquiry.annotations.Column;
import com.heinrichreimer.inquiry.annotations.Table;

@Table("persons")
public class Person {

    @Column(value = "name", unique = true)
    private String name;
    @Column("age")
    private int age;
    @Column("spouse")
    private SimplePerson spouse;

    public Person() {
    }

    public Person(String name, int age, SimplePerson spouse) {
        this.name = name;
        this.age = age;
        this.spouse = spouse;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + "'" +
                ", age=" + age +
                ", spouse=" + spouse +
                '}';
    }
}