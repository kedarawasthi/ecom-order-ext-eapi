package com.dev.models;

import lombok.Data;

@Data
public class RedriveRequest {
	String mainQueueName;
	String deadLetterQueueName;
	Integer maxNoOfMessagesToRedrive;
	@Override
	public String toString() {
		return "RedriveRequest [mainQueueName=" + mainQueueName + ", deadLetterQueueName=" + deadLetterQueueName
				+ ", maxNoOfMessagesToRedrive=" + maxNoOfMessagesToRedrive + "]";
	}
	public String getMainQueueName() {
		return mainQueueName;
	}
	public void setMainQueueName(String mainQueueName) {
		this.mainQueueName = mainQueueName;
	}
	public String getDeadLetterQueueName() {
		return deadLetterQueueName;
	}
	public void setDeadLetterQueueName(String deadLetterQueueName) {
		this.deadLetterQueueName = deadLetterQueueName;
	}
	public Integer getMaxNoOfMessagesToRedrive() {
		return maxNoOfMessagesToRedrive;
	}
	public void setMaxNoOfMessagesToRedrive(Integer maxNoOfMessagesToRedrive) {
		this.maxNoOfMessagesToRedrive = maxNoOfMessagesToRedrive;
	}
	
}
