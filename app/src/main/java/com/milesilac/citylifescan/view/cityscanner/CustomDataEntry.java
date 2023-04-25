package com.milesilac.citylifescan.view.cityscanner;
import com.anychart.chart.common.dataentry.DataEntry;

public class CustomDataEntry extends DataEntry {

    public CustomDataEntry(String x, Number value, Number percentile, Number salary) {
        setValue("x", x);
        setValue("value", value);
        setValue("percentile", percentile);
        setValue("salary", salary);
    }

}
