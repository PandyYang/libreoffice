package com.common.model;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
public class Result<T> {
    public static final int success = 0;
    public static final int fail = 1;
    private int status = success;
    private String message = "success";
    private T data;

    public Result setErrorMsgInfo(String msg){
        this.setStatus(fail);
        this.setMessage(msg);
        return this;

    }

}
