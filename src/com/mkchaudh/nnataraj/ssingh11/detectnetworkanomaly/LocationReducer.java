package com.mkchaudh.nnataraj.ssingh11.detectnetworkanomaly;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author team
 */
public class LocationReducer extends Reducer<Text, Text, Text, Text> {
    // The Karmasphere Studio Workflow Log displays logging from Apache Commons Logging, for example:
    // private static final Log LOG = LogFactory.getLog("com.nnataraj.HomeworkReducer");

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        int count = 0;
        ArrayList<Text> _values = new ArrayList<>();
        for (Text value : values) {
            count++;
            _values.add(value);
        }

        if (count > DetectNetworkAnomalyJob.ARG_HOUSEHOLD_COUNT) {
            for (Text value : _values) {
                String[] record = value.toString().split(",");
                context.write(new Text(record[3]),new Text(value.toString()+",yes"));
            }
        } else {
            for (Text value : _values) {
                String[] record = value.toString().split(",");
                context.write(new Text(record[3]),new Text(value.toString()+",no"));
            }
        }

    }
}