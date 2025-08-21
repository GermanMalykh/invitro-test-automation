package tests.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CitiesInfo {

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    @JsonProperty("ven_price")
    private String ven_price;

    @JsonProperty("obs158_price")
    private String obs158_price;

    @Override
    public String toString() {
        return name;
    }
}
