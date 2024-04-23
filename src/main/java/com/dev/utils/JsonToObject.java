package com.dev.utils;

import java.util.logging.Logger;

import org.mule.extension.sqs.api.model.Message;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.dev.models.RedriveRequest;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToObject {
public static String convertJsonToRedriveRequestObject(String jsonString) {
		
		Logger logger = Logger.getLogger(JsonToObject.class.getName());
		logger.info("convertJsonToRedriveRequestObject method invoked");
        
		ObjectMapper objMapper= new ObjectMapper();
		RedriveRequest redriveRequest = null;
		objMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        
        try {
        	redriveRequest = objMapper.readValue(jsonString, RedriveRequest.class);
			logger.info("Serailized Java Object: " + redriveRequest.toString());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
        return redriveRequest.toString();
        
	}

}
