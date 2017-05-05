package com.mkchaudh.nnataraj.ssingh11.detectnetworkanomaly;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nagaprasad on 5/5/17.
 */
public class UserProfileReducer extends Reducer<Text, Text, Text, Text> {

    private static final Double LOCATION_SPOOFING_FACTOR = 0.25;
    private static final Double PREVIOUS_AUTH_FAILURE = 0.30;
    private static final Double PREVIOUS_DIFFERENT_IP = 0.60;
    private static final Double PREVIOUS_DIFFERENT_DEVICE = 0.50;
    private static final Double SUCCESS_PREV_AUTH_FAIL_DIFF_IP_R_DEVICE = 0.60;

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        CyclicArrayList userData = new CyclicArrayList(DetectNetworkAnomalyJob.ARG_USERPROFILE_SIZE);
        ArrayList<String> _values = new ArrayList<>();

        for(Text value : values) {
            _values.add(value.toString());
        }

        for(String event : _values) {
            double anomalyFactor = getAnomalyFactor(event, userData.getRef());
            if (anomalyFactor >= DetectNetworkAnomalyJob.ARG_SET_ANOMALY_FACTOR) {
                context.write(new Text(event.toString()+","+anomalyFactor), new Text());
            }
            userData.add(event);
        }

    }

    private boolean checkIfAuthFailure(ArrayList<String> userData) {

        int checkNoOfRecords = 2;

        for (int i = 1; i <= checkNoOfRecords; i++) {
            try {
                String[] record = userData.get(userData.size()-i).split(",");
                if (record[6].contains("authentication failure"))
                    return true;
            } catch (Exception ae) {
                //Do nothing
            }
        }

        return false;

    }

    private boolean checkIfDifferentDevice(String currentDevice, ArrayList<String> userData) {

        int checkNoOfRecords = 5;

        currentDevice = currentDevice.split(" ")[0];


        for (int i = 1; i <= checkNoOfRecords; i++) {
            try {
                String[] record = userData.get(userData.size()-i).split(",");
                if (!record[7].contains(currentDevice))
                    return true;
            } catch (Exception ae) {
                //Do nothing
            }
        }

        return false;
    }

    private boolean checkIfDifferentIP(String currentIP, ArrayList<String> userData) {

        int checkNoOfRecords = 5;


        for (int i = 1; i <= checkNoOfRecords; i++) {
            try {
                String[] record = userData.get(userData.size()-i).split(",");
                if (record[5]!=currentIP)
                    return true;
            } catch (Exception ae) {
                //Do nothing
            }
        }

        return false;
    }

    private Double getAnomalyFactor(String event, ArrayList<String> userData) {
        ArrayList<Double> subAnomaly = new ArrayList<>();

        String[] record = event.split(",");

        if (record[8] == "yes") {
            //Location spoofing
            subAnomaly.add(LOCATION_SPOOFING_FACTOR);
        }

        if (userData.size() != 0) {
            boolean auth_fail = checkIfAuthFailure(userData);
            if (auth_fail)
                subAnomaly.add(PREVIOUS_AUTH_FAILURE);

            boolean diff_ip = checkIfDifferentIP(record[5], userData);
            if (diff_ip)
                subAnomaly.add(PREVIOUS_DIFFERENT_IP);

            boolean diff_device = checkIfDifferentDevice(record[7], userData);
            if (diff_device)
                subAnomaly.add(PREVIOUS_DIFFERENT_DEVICE);

            if (record[6].contains("logged in") && (diff_ip || diff_device) && auth_fail)
                subAnomaly.add(SUCCESS_PREV_AUTH_FAIL_DIFF_IP_R_DEVICE);
        }

        Double avg = 0.0;
        for (Double val : subAnomaly) {
            avg += val;
        }
        if (subAnomaly.size() > 1) {
            //avg /= 2;
        }
        return Math.round(avg*100)/100.00;
    }
}
