package tests.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductsInfo {

    @JsonProperty("ven_title")
    private String ven_title;

    @JsonProperty("obs158_title")
    private String obs158_title;

    @JsonProperty("citesInfo")
    private CitiesInfo[] citesInfo;

}
