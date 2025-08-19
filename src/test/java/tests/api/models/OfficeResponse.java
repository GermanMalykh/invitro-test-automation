package tests.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OfficeResponse {

    @JsonProperty("office")
    private OfficeData office;

    @JsonProperty("allProductsAvailable")
    private boolean allProductsAvailable;

    @JsonProperty("productAvailableCount")
    private int productAvailableCount;

    @JsonProperty("unavailableProducts")
    private Object unavailableProducts;
}
