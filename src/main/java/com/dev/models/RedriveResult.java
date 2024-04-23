package com.dev.models;

import lombok.Data;

@Data
public class RedriveResult {
	
	private String message;
	private String sendMessageBatchResult;
	private String deleteMessageBatchResult;
	
	@Override
	public String toString() {
		return "RedriveResult [sendMessageBatchResult=" + sendMessageBatchResult + ", deleteMessageBatchResult="
				+ deleteMessageBatchResult + "]";
	}
	public String getSendMessageBatchResult() {
		return sendMessageBatchResult;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setSendMessageBatchResult(String sendMessageBatchResult) {
		this.sendMessageBatchResult = sendMessageBatchResult;
	}
	public String getDeleteMessageBatchResult() {
		return deleteMessageBatchResult;
	}
	public void setDeleteMessageBatchResult(String deleteMessageBatchResult) {
		this.deleteMessageBatchResult = deleteMessageBatchResult;
	}

}
