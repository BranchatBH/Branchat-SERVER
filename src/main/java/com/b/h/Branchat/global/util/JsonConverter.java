package com.b.h.Branchat.global.util;

import com.b.h.Branchat.global.exception.GlobalErrorCode;
import com.b.h.Branchat.global.exception.JsonConvertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonConverter {

    private final ObjectMapper objectMapper;

    public String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonConvertException(GlobalErrorCode.JSON_CONVERSION_ERROR, e.getMessage());
        }
    }
}
