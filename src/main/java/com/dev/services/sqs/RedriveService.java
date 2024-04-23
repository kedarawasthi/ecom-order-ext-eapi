package com.dev.services.sqs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequest;
import com.amazonaws.services.sqs.model.DeleteMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.DeleteMessageBatchResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageBatchResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.dev.models.RedriveRequest;
import com.dev.models.RedriveResult;
import com.dev.utils.ObjectToJson;

public class RedriveService {

	private static Logger logger = Logger.getLogger(RedriveService.class.getName());

	private static AmazonSQS sqsClient;

	public static String redriveDlqMessages(String mainQueueName, String deadLetterQueueName,Integer maxNoOfMessagesToRedrive,String access_key,String secret_access_key) {

		/********************************* STEP 0: set sqs client ***********************************************************/
		
		AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_access_key);
		sqsClient = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider
                		(credentials))
                .withRegion(Regions.AP_SOUTHEAST_2)
                .build();
		
		/********************************* STEP 1: get dlqUrl and main queueUrl with names************************************/

		String mainQueueUrl = sqsClient.getQueueUrl(mainQueueName).getQueueUrl();
		String deadLetterQueueUrl = sqsClient.getQueueUrl(deadLetterQueueName).getQueueUrl();
		logger.info("======== MainQueueUrl: " + mainQueueUrl + " , DeadLetterUrl: " + deadLetterQueueUrl + " =====================");

		/***************************************** STEP 2: read messages from dlq*******************************************/

		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(deadLetterQueueUrl)
				.withMaxNumberOfMessages(maxNoOfMessagesToRedrive).withAttributeNames("All")
				.withMessageAttributeNames("All");
		ReceiveMessageResult receiveMessageResult = sqsClient.receiveMessage(receiveMessageRequest);
		if (receiveMessageResult.getMessages().isEmpty()) {
			logger.info("no messages to redrive");
			RedriveResult redriveResult = new RedriveResult();
			redriveResult.setMessage("no messages to redrive");
			String jsonString = ObjectToJson.convertRedriveResultObjectToJson(redriveResult);
			return jsonString;
		}
		logger.info("======== ReceiveMessageResult: " + receiveMessageResult.toString());

		/******************************** STEP 3: filter retryable errors from read message result *************************/

		
		 String retryableErrors = "HTTP:TIMEOUT,HTTP:INTERNAL_SERVER_ERROR,HTTP:BAD_GATEWAY,HTTP:SERVICE_UNAVAILABLE,HTTP:CONNECTIVITY";
		 List<Message> messages = receiveMessageResult.getMessages(); 
		 List<Message> retryableMessages = messages 
				 							.stream() 
				 							.filter(s->retryableErrors.contains(s.getMessageAttributes().get("errorType").getStringValue())) 
				 							.collect(Collectors.toList());
		 retryableMessages.forEach(item->
		 {
			 item.getMessageAttributes().remove("errorType");
			 item.getMessageAttributes().remove("errorDescription"); 
		 });
		 if (retryableMessages.isEmpty()) {
				logger.info("no retryable messages to redrive");
				RedriveResult redriveResult = new RedriveResult();
				redriveResult.setMessage("no retryable messages to redrive");
				String jsonString = ObjectToJson.convertRedriveResultObjectToJson(redriveResult);
				return jsonString;
		 }
		 logger.info("======== Retryable Messages ready to redrive: " + retryableMessages.toString());
		 

		/******************************** STEP 4: send batch messages to main queue ****************************************/

		
		 List<SendMessageBatchRequestEntry> sendMessageBatchRequestEntries = new ArrayList<>(); 
		 retryableMessages.forEach(item->
		 {
			 SendMessageBatchRequestEntry sendMessageBatchRequestEntry = new SendMessageBatchRequestEntry();
			 sendMessageBatchRequestEntry.setId(item.getMessageId());
			 sendMessageBatchRequestEntry.setMessageAttributes(item.getMessageAttributes()); 
			 sendMessageBatchRequestEntry.setMessageBody(item.getBody());
			  
			 sendMessageBatchRequestEntries.add(sendMessageBatchRequestEntry); 
		 });
			 
		 SendMessageBatchRequest sendMessageBatchRequest = new SendMessageBatchRequest(mainQueueUrl, sendMessageBatchRequestEntries);
		 SendMessageBatchResult sendMessageBatchResult = sqsClient.sendMessageBatch(sendMessageBatchRequest);
		 logger.info("======== Send Message Batch Result: " + sendMessageBatchResult.toString());
		 

		/******************************** STEP 5: delete batch message from dlq****************************************/

		
		  List<DeleteMessageBatchRequestEntry> deleteMessageBatchRequestEntries = new ArrayList<>(); retryableMessages.forEach(item->
		  {  
			  DeleteMessageBatchRequestEntry deleteMessageBatchRequestEntry = new DeleteMessageBatchRequestEntry();
			  deleteMessageBatchRequestEntry.setId(item.getMessageId());
			  deleteMessageBatchRequestEntry.setReceiptHandle(item.getReceiptHandle());
			  
			  deleteMessageBatchRequestEntries.add(deleteMessageBatchRequestEntry); 
		  });
		  DeleteMessageBatchRequest deleteMessageBatchRequest = new DeleteMessageBatchRequest(deadLetterQueueUrl,deleteMessageBatchRequestEntries); 
		  DeleteMessageBatchResult deleteMessageBatchResult = sqsClient.deleteMessageBatch(deleteMessageBatchRequest);
		  logger.info("======== Delete Message Batch Result: " + deleteMessageBatchResult.toString());

		/*********************************** STEP 6: form response object***************************************/

		
		  String sendMessageBatchResultString = ObjectToJson.convertSendMessageBatchResultObjectToJson(sendMessageBatchResult); 
		  String deleteMessageBatchResultString = ObjectToJson.convertDeleteMessageBatchResultObjectToJson(deleteMessageBatchResult); 
		  RedriveResult redriveResult=new RedriveResult();
		  redriveResult.setMessage("Redrive Successful");
		  redriveResult.setDeleteMessageBatchResult(deleteMessageBatchResultString);
		  redriveResult.setSendMessageBatchResult(sendMessageBatchResultString); 
		  String result = ObjectToJson.convertRedriveResultObjectToJson(redriveResult);
		  
		  logger.info("======== Redrive Result: " + result);
		 
		return result;

	}
	

//	public static void main(String args[]) {
//
//		System.out.println("Hello World");
//		String res= redriveDlqMessages("salesorder-customer-queue", "salesorder-customer-dlq", 1);
//		System.out.println(res);
//	}

}