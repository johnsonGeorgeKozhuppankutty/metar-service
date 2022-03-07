package com.twoeSystems.aviation.utility;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * JSONUtils .
 * @author The Johnson George.
 */
public class JSONUtils {

      static public <T> List<T> convertFromJsonToList(String json, TypeReference<List<T>> var) throws JsonParseException, JsonMappingException, IOException

    {

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(json, var);

    }

    

   
    static public <T> T covertFromJsonToObject(String json, Class<T> var) throws IOException{

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(json, var);

    }

 
    public static String covertFromObjectToJson(Object obj) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(obj);

    }


}
