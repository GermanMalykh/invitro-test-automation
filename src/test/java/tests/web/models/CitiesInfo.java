package tests.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CitiesInfo {

    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    private String id;

    @JsonProperty("venPrice")
    private String venPrice;

    @JsonProperty("obs158Price")
    private String obs158Price;

    @Override
    public String toString() {
        return name;
    }
}
