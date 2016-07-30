/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.example.RBP687.myapplication.backend;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.JsonArray;
import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.*;

import jdk.nashorn.internal.parser.JSONParser;

public class MyServlet extends HttpServlet {
    static Logger Log = Logger.getLogger("com.example.RBP687.myapplication.backend.MyServlet");

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String event = req.getParameter("event");
        String over = req.getParameter("over");
        if (event == null) {
            Log.severe("Original111");
            Date date = new Date();
            String dateTime = date.toString();
            Log.severe("Got cron message, adding::" + dateTime);
            //String url = "http://www.espncricinfo.com/ci/engine/match/1039957.html";
            //String url = "http://www.espncricinfo.com/zimbabwe-v-new-zealand-2016/engine/match/1024041.html";
            String url = "http://www.espncricinfo.com/sri-lanka-v-australia-2016/engine/match/995451.html";

            Log.severe("URL::  " + url);
            CricInfoExtract cric = new CricInfoExtract();
            String respString = cric.extractScore(url);

            Log.severe("Respose::" + respString);

            JsonObject content = new JsonParser().parse(respString).getAsJsonObject();
            Gson gson = new Gson();
            JsonParser p = new JsonParser();

            Set<Map.Entry<String, JsonElement>> entrySet = content.entrySet();

            String gameName = "";
            User newUser = null;
            for(Map.Entry<String, JsonElement> entry : entrySet) {
                gameName = entry.getKey();
                newUser = gson.fromJson(content.getAsJsonObject(entry.getKey()), User.class);
                Log.severe("GameName::" + gameName);
            }


            /*Firebase ref = new Firebase("https://my-first-app-14449.firebaseio.com/todoItems");

            Firebase alanRef = ref.child("users").child(gameName);
            //User alan = new User(newUser.getId(), newUser.getEvent(), newUser.getOver(), newUser.getUrl());
            alanRef.setValue(newUser);
*/
        }else {
            Log.severe("Dummy" + event);
            Firebase ref = new Firebase("https://my-first-app-14449.firebaseio.com/todoItems");
            Firebase alanRef = ref.child("users").child("Cricket");
            String dumyUrl = "http://www.espncricinfo.com/sri-lanka-v-australia-2016/engine/match/995451.html";
            User alan = new User(125, event, over, dumyUrl);
            alanRef.setValue(alan);
        }
    }
}
