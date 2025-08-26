package tests.android.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartData {

    @JsonProperty("productsInfo")
    private ProductsInfo[] productsInfo;
}
