package tests.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductsInfoResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("article")
    private String article;

    @JsonProperty("shortName")
    private String shortName;

    @JsonProperty("name")
    private String name;

    @JsonProperty("unitName")
    private String unitName;

    @JsonProperty("type")
    private String type;

    @JsonProperty("classifierData")
    private ClassifierData classifierData;

    @JsonProperty("interpretationDescription")
    private String interpretationDescription;

    @JsonProperty("textInterpretation")
    private String textInterpretation;

    @JsonProperty("textStatement")
    private String textStatement;

    @JsonProperty("textPreparation")
    private String textPreparation;

    @JsonProperty("price")
    private double price;

}
