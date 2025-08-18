package tests.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClassifierData {

    @JsonProperty("classifierType")
    private String classifierType;

    @JsonProperty("hasQuantity")
    private boolean hasQuantity;

    @JsonProperty("hasPriority")
    private boolean hasPriority;

    @JsonProperty("planningAvailable")
    private boolean planningAvailable;

    @JsonProperty("selectable")
    private boolean selectable;

    @JsonProperty("aggregated")
    private boolean aggregated;

    @JsonProperty("dependent")
    private boolean dependent;

    @JsonProperty("service")
    private boolean service;

    @JsonProperty("requiredService")
    private boolean requiredService;

    @JsonProperty("requirePatient")
    private boolean requirePatient;

    @JsonProperty("requireDelivery")
    private boolean requireDelivery;

    @JsonProperty("hasDeadline")
    private boolean hasDeadline;

    @JsonProperty("printTaxDeduction")
    private boolean printTaxDeduction;

    @JsonProperty("printEstimate")
    private boolean printEstimate;
}
