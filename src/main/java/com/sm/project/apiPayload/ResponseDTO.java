package com.sm.project.apiPayload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sm.project.apiPayload.code.BaseCode;
import com.sm.project.apiPayload.code.status.SuccessStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ResponseDTO<T> {

    @JsonPropertyOrder("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public static <T> ResponseDTO<T> onSuccess(T result) {
        return new ResponseDTO<>(true, SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), result);
    }

    public static <T> ResponseDTO<T> of(BaseCode code, T result) {
        return new ResponseDTO<>(true, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), result);
    }

    public static <T> ResponseDTO<T> onFailure(String code, String message, T data) {
        return new ResponseDTO<>(false, code, message, data);
    }
}

