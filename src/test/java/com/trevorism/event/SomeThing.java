package com.trevorism.event;

import java.util.Date;

/**
 * @author tbrooks
 */
public final class SomeThing {

    private final String name;
    private final int num;
    private final Date date;

    public SomeThing(String name, int num, Date date) {

        this.name = name;
        this.num = num;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public int getNum() {
        return num;
    }

    public Date getDate() {
        return date;
    }
}
