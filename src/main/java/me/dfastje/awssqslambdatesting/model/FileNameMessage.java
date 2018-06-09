package me.dfastje.awssqslambdatesting.model;

import lombok.Builder;

@Builder
public class FileNameMessage {

    String s3Bucket;
    String fileName;

}
