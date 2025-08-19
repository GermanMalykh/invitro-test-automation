package tests.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OfficeData {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private String address;

    @JsonProperty("externalPhone")
    private String externalPhone;

    @JsonProperty("longitude")
    private double longitude;

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("onlineRegistrationSupported")
    private boolean onlineRegistrationSupported;

    @JsonProperty("cityId")
    private String cityId;

    @JsonProperty("tags")
    private Object tags;

}
