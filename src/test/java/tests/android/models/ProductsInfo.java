package tests.android.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductsInfo {

    @JsonProperty("cityName")
    private String cityName;

    @JsonProperty("items")
    private ItemsInfo[] items;

    @JsonProperty("totalPrice")
    private String totalPrice;

    @Override
    public String toString() {
        return cityName;
    }

}
