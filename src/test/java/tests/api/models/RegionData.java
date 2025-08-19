package tests.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegionData {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;
}
