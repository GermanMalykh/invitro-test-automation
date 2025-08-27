package tests.android.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Sections {

    @JsonProperty("allResults")
    private String allResults;

    @JsonProperty("dynamicResults")
    private String dynamicResults;

    @JsonProperty("myRecords")
    private String myRecords;

    @JsonProperty("myOrders")
    private String myOrders;

    @JsonProperty("analyses")
    private String analyses;

    @JsonProperty("medicalOrder")
    private String medicalOrder;

    @JsonProperty("homeOrder")
    private String homeOrder;

    @JsonProperty("addresses")
    private String addresses;

}
