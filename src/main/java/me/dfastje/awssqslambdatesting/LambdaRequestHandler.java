package me.dfastje.awssqslambdatesting;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import me.dfastje.awssqslambdatesting.model.FileNameMessage;
import me.dfastje.awssqslambdatesting.util.S3FileReader;
import me.dfastje.awssqslambdatesting.util.SQSMessageWriter;

@NoArgsConstructor
public class LambdaRequestHandler implements RequestHandler<S3Event, String> {

    Gson gson = new Gson();
    S3FileReader s3FileReader = new S3FileReader();
    SQSMessageWriter sqsMessageWriter = new SQSMessageWriter();
    String queueUrl;
    String sqsQueueEnvName = "QueueURL";

    @Override
    public String handleRequest(S3Event event, Context context) {

        queueUrl = System.getenv(sqsQueueEnvName);
        process(event);
        return "Success";
    }

    public void process(S3Event s3Event){
        s3Event.getRecords().forEach( rec -> {
            String s3Bucket = rec.getS3().getBucket().getName();
            String s3Object = rec.getS3().getObject().getKey();
            FileNameMessage fileNameMessage = FileNameMessage.builder()
                    .fileName(s3Object)
                    .s3Bucket(s3Bucket)
                    .build();
            String jsonMessage = gson.toJson(fileNameMessage);
            sqsMessageWriter.sendMessage(queueUrl, jsonMessage);
        });
    }
}
