package com.aje.onemenu.activities;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public  class Client extends AsyncTask<Void ,Void , Void > {

    private String userID;

    public void setUserID(String id){

        userID = id;
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        final String adress = "35.225.24.218"; //"34.94.222.68";
        final int Port = 8444;
        Log.d("Connection","trying to create connection");
        try {
            Log.d("Connection", "Creating socket");
            Socket connect = new Socket(adress, Port);
            Log.d("Connection","Connected");
            DataOutputStream dout = new DataOutputStream(connect.getOutputStream());
            DataInputStream din = new DataInputStream(connect.getInputStream());
            //dout.writeUTF(" yyy" + userID);
            dout.write((" yyy" + userID).getBytes("UTF-8"));
            //dout.write((" yyy" + userID).getBytes());
            //dout.writeBytes(" yyy" + userID);
            dout.flush();
            Log.d("Connection","Sent");
            dout.close();
            din.close();
            connect.close();
        }catch (IOException e){
            {
                e.printStackTrace();
                Log.d("connection",e.getMessage());
            }}
        return null;
    }
}