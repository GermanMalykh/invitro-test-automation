package tests.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductsInfo {

    @JsonProperty("venTitle")
    private String venTitle;

    @JsonProperty("obs158Title")
    private String obs158Title;

    @JsonProperty("citiesInfo")
    private CitiesInfo[] citiesInfo;

}
