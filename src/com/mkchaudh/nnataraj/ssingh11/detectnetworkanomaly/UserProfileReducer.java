package com.mkchaudh.nnataraj.ssingh11.detectnetworkanomaly;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nagaprasad on 5/5/17.
 */
public class UserProfileReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        CyclicArrayList userData = new CyclicArrayList(DetectNetworkAnomalyJob.ARG_USERPROFILE_SIZE);
        ArrayList<String> _values = new ArrayList<>();

        for(Text value : values) {
            userData.add(value.toString());
            _values.add(value.toString());
        }

        for(String event : _values) {
            double anomalyFactor = getAnomalyFactor(event, userData.getRef());
            if (anomalyFactor >= DetectNetworkAnomalyJob.ARG_SET_ANOMALY_FACTOR) {
                context.write(new Text(event.toString()+","+anomalyFactor), new Text());
            }
        }

    }

    private double getAnomalyFactor(String event, ArrayList<String> userData) {
        return 0;
    }
}
