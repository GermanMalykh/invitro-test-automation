package tests.android.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CitySectionsData {

    @JsonProperty("cityInfo")
    private CityInfo[] cityInfo;

}
