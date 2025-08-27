package tests.android.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CityInfo {

    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("sections")
    private Sections sections;

    @Override
    public String toString() {
        return cityName;
    }

}
