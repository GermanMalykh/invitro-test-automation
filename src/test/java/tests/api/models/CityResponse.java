package tests.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CityResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("region")
    private RegionData region;

    @JsonProperty("territory")
    private String territory;

    @JsonProperty("capital")
    private boolean capital;

    @JsonProperty("homeVisitAvailable")
    private boolean homeVisitAvailable;

    @JsonProperty("onlineRegistrationSupported")
    private boolean onlineRegistrationSupported;
}
