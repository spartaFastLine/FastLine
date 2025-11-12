package com.fastline.hubservice.infrastructure.config;

import com.fastline.common.exception.CustomException;
import com.fastline.common.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignErrorConfig implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 401) {
            return new CustomException(ErrorCode.UNAUTHORIZED); // 외부인증 api 안됨
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
