package com.example.RBP687.myapplication.backend;


import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.JsonArray;
import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;
import com.google.appengine.repackaged.com.google.gson.JsonSyntaxException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CricInfoExtract  {
  private  final long serialVersionUID = 1L;
  private  final Gson gson = new Gson();
  public final  Logger log = Logger
      .getLogger(CricInfoExtract.class.getName());

  public String extractScore(String url) {
    log.setLevel(Level.ALL);
    log.info("Cricinfo Extract Request started");
    String responseJson = "";
    String jsonResponse = EMPTYBRACE;
    //response.setContentType("application/json");
    //String url = request.getParameter("url");
    String matchId = null;
    for (String urlpart : url.split("/")) {
      if(urlpart.endsWith("html")){
        matchId = urlpart.replaceAll("\\D", "");
      }
    }
    if(matchId != null) {
      String modifiedUrl = "http://www.espncricinfo.com/netstorage/" + matchId + ".json?xhr=1";
      log.info("url: " + url);
      jsonResponse = pullReviews(modifiedUrl, url);
    }
    return jsonResponse;
    /*PrintWriter out = response.getWriter();
    out.print(jsonResponse);
    out.flush();
    out.close();*/
  }

  private String pullReviews(String url, String sourceUrl) {
    ArrayList<JsonObject> results = new ArrayList<JsonObject>();
    JsonObject reviews = new JsonObject();
    int i = 1;
    String htmlContent = wsCall(url);
    if (htmlContent == null || !isJSONValid(htmlContent)) {
      return EMPTYBRACE;
    }
    JsonObject content = new JsonParser().parse(htmlContent).getAsJsonObject();
    if (content.get("comms") == null) {
      return EMPTYBRACE;
    }
    JsonArray json = content.get("comms").getAsJsonArray();
    if(json.size() > 0 && json.get(0) != null){
      for (JsonElement reviewDetails : json.get(0).getAsJsonObject().get("ball").getAsJsonArray()) {
        if(reviewDetails.getAsJsonObject().get("event") != null){
          log.info("comms_id: " + reviewDetails.getAsJsonObject().get("comms_id"));
          log.info("event: " + reviewDetails.getAsJsonObject().get("event"));
          JsonObject obj = new JsonObject();
            String commentId =reviewDetails.getAsJsonObject().get("comms_id").getAsString();
            Random ran = new Random();
            int id = !commentId.equalsIgnoreCase("") ? Integer.parseInt(commentId) : ran.nextInt(6) + 5;
          obj.addProperty("id", id);
          obj.addProperty("over", reviewDetails.getAsJsonObject().get("overs_actual").getAsString());
          obj.addProperty("event", reviewDetails.getAsJsonObject().get("event").getAsString());
          /*String urlLink = content.getAsJsonObject().get("other_scores").getAsJsonObject().get("international")
              .getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
          obj.addProperty("url", "http://www.espncricinfo.com" + urlLink);*/
          obj.addProperty("url", sourceUrl);
          reviews.add("Cricket", new Gson().toJsonTree(obj));
          break;
        }
      }
    }

    String jsonresponse = reviews.toString();
    return jsonresponse;
  }

  public boolean isJSONValid(String jsonInString) {
    try {
        gson.fromJson(jsonInString, Object.class);
        return true;
    } catch(JsonSyntaxException ex) {
        return false;
    }
}

  private String wsCall(String hostURL) {
    int retries = 1;
      String htmlContent = "";
      Connection.Response responses = null;

      do {
        try {
          Thread.sleep(THOUSAND);
        } catch (InterruptedException e) {
          log.info(EXCEPTION_COLON + e.getMessage());
        }
        try {
          responses = Jsoup.connect(hostURL)
              .header("Accept-Encoding", "gzip, deflate")
              .ignoreContentType(true)
              //.ignoreHttpErrors(true)
              .maxBodySize(0)
              .timeout(0)
              .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 "
                          + "(KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36")
              .execute();
        } catch (IOException e) {
          log.info(EXCEPTION_COLON + e.getMessage());
        }
        retries++;
      } while (responses.statusCode() != TWO_HUNDRED && retries <= THIRTY);

      if (responses.statusCode() == TWO_HUNDRED) {
        htmlContent = responses.body();
      }
      return htmlContent;
  }

  /** Constant for empty brace. */
  public  final String EMPTYBRACE = "{}";

  /** Constant for logging. */
  public  final String EXCEPTION_COLON = "Exception: ";

  /** Constant to represent 30. */
  public  final int THIRTY = 30;

  /** Constant to represent Status Code 200. */
  public  final int TWO_HUNDRED = 200;

  /** Constant to represent 204. */
  public  final int TWO_HUNDRED_AND_FORTY = 204;

  /** Constant to represent Status Code 403. */
  public  final int FOUR_NOT_THREE = 403;

  /** Constant to represent Status Code 500. */
  public  final int FIVE_HUNDRED = 500;

  /** Constant to represent 1000. */
  public  final int THOUSAND = 1000;

  /** Constant to represent 1000 long. */
  public  final long THOUSAND_LONG = 1000L;

  /** Constant to represent Status Code 2000. */
  public  final int TWO_THOUSAND = 2000;

  /** Constant to represent Status Code 5000. */
  public  final int FIVE_THOUSAND = 5000;
}
