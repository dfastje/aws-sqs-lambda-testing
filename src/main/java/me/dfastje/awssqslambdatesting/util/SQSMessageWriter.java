package me.dfastje.awssqslambdatesting.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SQSMessageWriter {
    AmazonSQS sqs;

    public SQSMessageWriter(){
        AmazonSQSClientBuilder sqsClientBuilder = AmazonSQSClientBuilder.standard()
                .withRegion(Regions.US_EAST_1);
        this.sqs = sqsClientBuilder.build();
    }

    public void sendMessage(String queueUrl, String message){
        SendMessageRequest messageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);
        sqs.sendMessage(messageRequest);
    }
}
