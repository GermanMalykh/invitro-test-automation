package tests.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErrorResponse {

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("error")
    private String error;

    @JsonProperty("errors")
    private Object[] errors;

    @JsonProperty("message")
    private String message;

    @JsonProperty("exception")
    private String exception;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("attempts")
    private Integer attempts;

    @JsonProperty("timeToResend")
    private Integer timeToResend;
}
