package com.dev.utils;

import java.io.IOException;
import java.util.logging.Logger;

import org.mule.extension.sqs.api.model.BatchResult;
import org.mule.extension.sqs.api.model.Message;

import com.amazonaws.services.sqs.model.DeleteMessageBatchResult;
import com.amazonaws.services.sqs.model.SendMessageBatchResult;
import com.dev.models.RedriveResult;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectToJson {

	public static String convertMessageObjectToJson(Object obj) {
		
		Logger logger = Logger.getLogger(ObjectToJson.class.getName());
		logger.info("convertMessageObjectToJson method invoked");
        
		Message msg = (Message) obj;
		ObjectMapper objMapper= new ObjectMapper();
        objMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        String jsonObj="";
    
        try {
			jsonObj = objMapper.writeValueAsString(msg);
			logger.info("Deserailized Json Object: " + jsonObj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
        return jsonObj;
        
	}
	
	public static String convertBatchResultObjectToJson(Object obj) {
		
		Logger logger = Logger.getLogger(ObjectToJson.class.getName());
		logger.info("convertBatchResultObjectToJson method invoked");
        
		BatchResult msg = (BatchResult) obj;
		ObjectMapper objMapper= new ObjectMapper();
        objMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        String jsonObj="";
        
        try {
			jsonObj = objMapper.writeValueAsString(msg);
			logger.info("Deserailized Json Object: " + jsonObj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
        return jsonObj;
        
	}
	
	public static String convertSendMessageBatchResultObjectToJson(Object obj) {
		
		Logger logger = Logger.getLogger(ObjectToJson.class.getName());
		logger.info("convertSendMessageBatchResultObjectToJson method invoked");
        
		SendMessageBatchResult msg = (SendMessageBatchResult) obj;
		ObjectMapper objMapper= new ObjectMapper();
        objMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        String jsonObj="";
        
        try {
			jsonObj = objMapper.writeValueAsString(msg);
			logger.info("Deserialized Json Object: " + jsonObj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
        return jsonObj;
        
	}
	
	public static String convertDeleteMessageBatchResultObjectToJson(Object obj) {
		
		Logger logger = Logger.getLogger(ObjectToJson.class.getName());
		logger.info("convertDeleteMessageBatchResultObjectToJson method invoked");
        
		DeleteMessageBatchResult msg = (DeleteMessageBatchResult) obj;
		ObjectMapper objMapper= new ObjectMapper();
        objMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        String jsonObj="";
        
        try {
			jsonObj = objMapper.writeValueAsString(msg);
			logger.info("Deserialized Json Object: " + jsonObj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
        return jsonObj;
	}
	
	public static String convertRedriveResultObjectToJson(Object obj) {
		
		Logger logger = Logger.getLogger(ObjectToJson.class.getName());
		logger.info("convertRedriveResultResultObjectToJson method invoked");
        
		RedriveResult msg = (RedriveResult) obj;
		ObjectMapper objMapper= new ObjectMapper();
        objMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        String jsonObj="";
        
        try {
			jsonObj = objMapper.writeValueAsString(msg);
			logger.info("Deserialized Json Object: " + jsonObj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        
        return jsonObj;
	}
	




}