package com.example.administrator.speedviocatcher;

/**
 * Created by Administrator on 1/5/2018.
 */
import java.util.*;

public class Person {
    private String v_num;
    private String v_ticket;
    private String v_penalty;
    private String v_status;

    public Person(String num, String ticket, String penalty, String status) {
        v_num = num;
        v_ticket = ticket;
        v_penalty = penalty;
        v_status = status;
    }

    public String getNum() {
        return v_num;
    }

    public String getTicket() {
        return v_ticket;
    }

    public String getPenalty() {
        return v_penalty;
    }

    public String getStatus() {
        return v_status;
    }

    public String toString() {
        return "name: " + v_num + ", age: " +
                ", course: " + v_ticket + ", year: " + v_penalty +
                ", section: " + v_status;
    }
}