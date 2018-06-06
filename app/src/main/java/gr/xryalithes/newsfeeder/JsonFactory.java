package gr.xryalithes.newsfeeder;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.util.TimeZone;

public final class JsonFactory {
    private static final String LOG_TAG = "News Feeder";

    private JsonFactory() {
    }
    /////////////METHOD FOR CREATING AN URL OBJECT FROM A STRING/////////////////////////////
    private static URL makeUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
    ///////////CONNECTING........./////////////////////////////////////////////////////////////
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            ///////////CONVERTING DATA TO STRING!//////////////////////////////////////////////
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;// <-------THIS STRING IS THE FINAL RESULT///////////////////
    }
    /////////THIS METHOD CREATES THE STRING FROM THE DOWNLOADED RAW DATA///////////////////////////////////////////
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    ////////  HERE WE PARSE THE JSON FILE (STRING) AND GET THE VALUES FOR SHOWING TO USER////////////////
    public static ArrayList<Article> extractDataFromJson(String jsonResponse) {
        ArrayList<Article> articles = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(jsonResponse);
            JSONObject response = rootObject.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject currentObject = resultsArray.getJSONObject(i);
                String section = currentObject.getString("sectionName");
                String title = currentObject.getString("webTitle");
                String date = currentObject.getString("webPublicationDate");
                String articleUrl = currentObject.getString("webUrl");
                String image = null;
                String contributor = null;
                if (currentObject.has("fields")) { ////some older articles don't have this field,leading to json parsing error if we try to parse them.So we much check.
                    JSONObject fieldsObject = currentObject.getJSONObject("fields");
////////////////////IF THERE IS AUTHOR , GET IT///////////////////////////////////////////////////
                    if (fieldsObject.has("byline")) {
                        contributor = fieldsObject.getString("byline");
                    }
                    //////////////IF THERE IS A THUMBNAIL FIELD,  GET IT//////
                    if (fieldsObject.has("thumbnail")) {
                        image = fieldsObject.getString("thumbnail");
                    }
                }
//////////////////////////// FORMATTING THE JSON DATE/////////////////////////////
                // 2018-05-30 T 13:14:07 Z
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'", Locale.UK);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date dateformatted = null;
                try {
                    dateformatted = sdf.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String dateFinal = formatDate(dateformatted);

                Article article = new Article(section, title, dateFinal, contributor, articleUrl, image);
                articles.add(article);
            }

        } catch (JSONException e) {

            Log.e("JsonFactory", "Problem parsing the article JSON results", e);
        }
        return articles;
    }

    ///////// THE MAIN METHOD THAT TRIGGERS ALL  (1.MAKING URL 2. CONNECT 3.EXTRACT DATA 4.RETURN A POPULATED ARRAYLIST!!/////
    public static List<Article> fetchArticleData(String requestUrl) {
        URL url = makeUrl(requestUrl);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Article> articles = extractDataFromJson(jsonResponse);
        return articles;
    }

    private static String formatDate(Date date) {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM dd, yyyy  hh:mm", Locale.ENGLISH);
        newDateFormat.setTimeZone(TimeZone.getDefault());
        String formattedDate = newDateFormat.format(date);
        return formattedDate;
    }
}