package com.lmw.widget.lmwrefreshlayout.lib.model;

public enum Orientation {
    NONE(0),
    HORIZONTAL(1),
    VERTICAL(2);
    private int value;

    Orientation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
