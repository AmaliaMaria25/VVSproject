package com.vvs.Webserver.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class ParseJson {
    private static ObjectMapper objectMapper = getDefaultObjectMapper();

    public static ObjectMapper getDefaultObjectMapper(){
        ObjectMapper objMapper = new ObjectMapper();
        objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objMapper;
    }

    public static JsonNode parse(String source) throws JsonProcessingException {
        return objectMapper.readTree(source);
    }

    public static <A> A parsedJson(JsonNode node, Class<A> classWeParseTo) throws JsonProcessingException {
        return objectMapper.treeToValue(node, classWeParseTo);
    }

    public static JsonNode parseToJson(Object o){
        return objectMapper.valueToTree(o);
    }

    public static String writeJson(Object o, boolean format) throws JsonProcessingException {
        ObjectWriter objectWriter = objectMapper.writer();
        if(format){
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objectWriter.writeValueAsString(o);
    }
}
