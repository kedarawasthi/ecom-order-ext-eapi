package com.dev.services.sqs;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

public class AwsSqsClient {

	private static String access_key;
	
	private static String secret_access_key;
	
	private static AmazonSQS single_sqs_client_instance = null;
	
    private AwsSqsClient()
    {}
 
    public static synchronized AmazonSQS getInstance()
    {
        if (single_sqs_client_instance == null)
        {
        	AWSCredentials credentials = new BasicAWSCredentials(access_key, secret_access_key);
        	single_sqs_client_instance = AmazonSQSClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider
                    		(credentials))
                    .withRegion(Regions.AP_SOUTHEAST_2)
                    .build();
        }
        return single_sqs_client_instance;
    }

}
