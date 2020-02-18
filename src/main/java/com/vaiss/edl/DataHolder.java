package com.vaiss.edl;

public class DataHolder {
    private String name;
    private Integer index;
    private Long time;

    public void setParams(String name, Integer index, Long time) {
        this.name = name;
        this.index = index;
        this.time = time;
    }

    @Override
    public String toString() {
        return String.join(",", name, index.toString(), time.toString());
    }

    public String getName() {
        return name;
    }

    public Integer getIndex() {
        return index;
    }

    public Long getTime() {
        return time;
    }
}
