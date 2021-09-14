package com.lingber.android_datagridview;

public class SellerBean {

    private Integer index;
    private String name;
    private Integer age;
    private String sex;
    private String department;
    private String rank;
    private Double sales;

    public SellerBean(Integer index, String name, Integer age, String sex, String department, String rank, Double sales) {
        this.index = index;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.department = department;
        this.rank = rank;
        this.sales = sales;
    }

    public SellerBean() {
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setSales(Double sales) {
        this.sales = sales;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getSex() {
        return sex;
    }

    public String getDepartment() {
        return department;
    }

    public String getRank() {
        return rank;
    }

    public Double getSales() {
        return sales;
    }
}
