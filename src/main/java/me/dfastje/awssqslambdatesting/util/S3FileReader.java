package me.dfastje.awssqslambdatesting.util;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * Notes:
 *  -I hard coded this to the us-east-1 region
 */
@AllArgsConstructor
@Log4j2
public class S3FileReader {


    AmazonS3 s3;

    public S3FileReader(){
        AmazonS3ClientBuilder s3ClientBuilder = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_EAST_1);
        this.s3 = s3ClientBuilder.build();
    }

    public String readS3File(String bucketName, String fileKey){

        S3Object s3Object = s3.getObject(bucketName, fileKey);

        try(InputStream inputStreamS3File = s3Object.getObjectContent();){

            ByteSource byteSource = new ByteSource(){
                @Override
                public InputStream openStream() throws IOException{
                    return inputStreamS3File;
                }
            };
            return byteSource.asCharSource(Charsets.UTF_8).read();

        } catch (IOException e){
            log.error("IOException in reading file from s3", e);
            throw new UncheckedIOException(e);
        }

    }

}
