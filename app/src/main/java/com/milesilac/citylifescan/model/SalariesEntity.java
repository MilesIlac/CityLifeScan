package com.milesilac.citylifescan.model;

import com.google.gson.annotations.SerializedName;

public class SalariesEntity {

    public Entity job;

    @SerializedName("salary_percentiles")
    public Entity salaryPercentiles;
}
