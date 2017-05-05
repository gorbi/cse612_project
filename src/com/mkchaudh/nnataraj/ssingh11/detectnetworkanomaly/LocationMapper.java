package com.mkchaudh.nnataraj.ssingh11.detectnetworkanomaly;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


/**
 * @author team
 */
public class LocationMapper extends Mapper<Text, Text, Text, Text> {
    // The Karmasphere Studio Workflow Log displays logging from Apache Commons Logging, for example:
    // private static final Log LOG = LogFactory.getLog("com.nnataraj.HomeworkMapper");

    @Override
    protected void map(Text key, Text value, Context context)
            throws IOException, InterruptedException {
        if (!key.toString().startsWith("#")) {
            String[] record = key.toString().split(",");
            context.write(new Text(record[5])/* IP is the key */,new Text(key.toString()));
        }
    }
}