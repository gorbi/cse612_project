package com.mkchaudh.nnataraj.ssingh11.detectnetworkanomaly;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by nagaprasad on 5/5/17.
 */
public class UserProfileMapper extends Mapper<Text, Text, Text, Text> {
    @Override
    protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
        context.write(key,value);
    }
}
