package gr.xryalithes.newsfeeder;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


public final class JsonFactory {


    private static final String LOG_TAG = "News Feeder";


    private JsonFactory() {
    }

    private static URL makeUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


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


        return jsonResponse;

    }


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

    public static ArrayList<Article> extractDataFromJson(String jsonResponse) {

        String jsonTESTString = "{\"response\":{\"status\":\"ok\",\"userTier\":\"developer\",\"total\":149454,\"startIndex\":1,\"pageSize\":10,\"currentPage\":1,\"pages\":14946,\"orderBy\":\"newest\",\"results\":[{\"id\":\"football/ng-interactive/2018/may/29/david-squires-on-liverpools-champions-league-final-heartache\",\"type\":\"interactive\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2018-05-29T11:02:13Z\",\"webTitle\":\"David Squires on ... Liverpool's Champions League final heartache\",\"webUrl\":\"https://www.theguardian.com/football/ng-interactive/2018/may/29/david-squires-on-liverpools-champions-league-final-heartache\",\"apiUrl\":\"https://content.guardianapis.com/football/ng-interactive/2018/may/29/david-squires-on-liverpools-champions-league-final-heartache\",\"fields\":{\"byline\":\"David Squires\"},\"references\":[],\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"}," +
                "{\"id\":\"football/blog/2018/may/29/liverpool-capture-fabinho-anfield-jurgen-klopp\",\"type\":\"article\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2018-05-29T09:55:00Z\",\"webTitle\":\"Liverpool’s £40m capture of fabulous Fabinho shows they mean business\",\"webUrl\":\"https://www.theguardian.com/football/blog/2018/may/29/liverpool-capture-fabinho-anfield-jurgen-klopp\",\"apiUrl\":\"https://content.guardianapis.com/football/blog/2018/may/29/liverpool-capture-fabinho-anfield-jurgen-klopp\",\"fields\":{\"byline\":\"Ed Aarons\"},\"references\":[],\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"football/2018/may/29/footballer-raheem-sterling-defends-gun-tattoo\",\"type\":\"article\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2018-05-29T09:25:26Z\",\"webTitle\":\"Raheem Sterling defends gun tattoo against media criticism\",\"webUrl\":\"https://www.theguardian.com/football/2018/may/29/footballer-raheem-sterling-defends-gun-tattoo\",\"apiUrl\":\"https://content.guardianapis.com/football/2018/may/29/footballer-raheem-sterling-defends-gun-tattoo\",\"fields\":{\"byline\":\"Nicola Slawson\"},\"references\":[],\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"}" +
                ",{\"id\":\"football/2018/may/29/womens-league-revamp-winners-and-losers\",\"type\":\"article\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2018-05-29T09:00:18Z\",\"webTitle\":\"WSL revamp winners and losers: from Manchester United to Watford via Lewes | Suzanne Wrack\",\"webUrl\":\"https://www.theguardian.com/football/2018/may/29/womens-league-revamp-winners-and-losers\",\"apiUrl\":\"https://content.guardianapis.com/football/2018/may/29/womens-league-revamp-winners-and-losers\",\"fields\":{\"byline\":\"Suzanne Wrack\"},\"references\":[],\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"football/2018/may/29/football-transfer-rumours-kylian-mbappe-to-manchester-city\",\"type\":\"article\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2018-05-29T08:12:15Z\",\"webTitle\":\"Football transfer rumours: Kylian Mbappé to Manchester City?\",\"webUrl\":\"https://www.theguardian.com/football/2018/may/29/football-transfer-rumours-kylian-mbappe-to-manchester-city\",\"apiUrl\":\"https://content.guardianapis.com/football/2018/may/29/football-transfer-rumours-kylian-mbappe-to-manchester-city\",\"fields\":{\"byline\":\"Paul Doyle\"},\"references\":[],\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"football/2018/may/28/gary-cahill-chelsea-form-miss-world-cup-england\",\"type\":\"article\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2018-05-28T21:30:04Z\",\"webTitle\":\"Gary Cahill feared Chelsea form would cost him England World Cup place\",\"webUrl\":\"https://www.theguardian.com/football/2018/may/28/gary-cahill-chelsea-form-miss-world-cup-england\",\"apiUrl\":\"https://content.guardianapis.com/football/2018/may/28/gary-cahill-chelsea-form-miss-world-cup-england\",\"fields\":{\"byline\":\"Martha Kelner\"},\"references\":[],\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"football/2018/may/28/liverpool-fabinho-monaco-close-signing\",\"type\":\"article\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2018-05-28T19:28:36Z\",\"webTitle\":\"Liverpool confirm £40m deal to sign Fabinho from Monaco\",\"webUrl\":\"https://www.theguardian.com/football/2018/may/28/liverpool-fabinho-monaco-close-signing\",\"apiUrl\":\"https://content.guardianapis.com/football/2018/may/28/liverpool-fabinho-monaco-close-signing\",\"fields\":{\"byline\":\"Ed Aarons\"},\"references\":[],\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"football/2018/may/29/jo-fernandes-the-australian-woman-making-world-cup-history-in-russia\",\"type\":\"article\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2018-05-28T18:00:00Z\",\"webTitle\":\"Jo Fernandes: the Australian woman making World Cup history | Kieran Pender\",\"webUrl\":\"https://www.theguardian.com/football/2018/may/29/jo-fernandes-the-australian-woman-making-world-cup-history-in-russia\",\"apiUrl\":\"https://content.guardianapis.com/football/2018/may/29/jo-fernandes-the-australian-woman-making-world-cup-history-in-russia\",\"fields\":{\"byline\":\"Kieran Pender\"},\"references\":[],\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"football/live/2018/may/28/coventry-city-v-exeter-city-league-two-play-off-final-live\",\"type\":\"liveblog\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2018-05-28T16:21:13Z\",\"webTitle\":\"Coventry promoted to League One after 3-1 play-off win over Exeter – as it happened\",\"webUrl\":\"https://www.theguardian.com/football/live/2018/may/28/coventry-city-v-exeter-city-league-two-play-off-final-live\",\"apiUrl\":\"https://content.guardianapis.com/football/live/2018/may/28/coventry-city-v-exeter-city-league-two-play-off-final-live\",\"fields\":{\"byline\":\"Nick Ames\"},\"references\":[],\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"},{\"id\":\"football/2018/may/28/coventry-city-exeter-city-league-two-play-off-final-match-report\",\"type\":\"article\",\"sectionId\":\"football\",\"sectionName\":\"Football\",\"webPublicationDate\":\"2018-05-28T16:15:42Z\",\"webTitle\":\"Jordan Willis sends Coventry City into play-off final rapture against Exeter City\",\"webUrl\":\"https://www.theguardian.com/football/2018/may/28/coventry-city-exeter-city-league-two-play-off-final-match-report\",\"apiUrl\":\"https://content.guardianapis.com/football/2018/may/28/coventry-city-exeter-city-league-two-play-off-final-match-report\",\"fields\":{\"byline\":\"Ben Fisher at Wembley\"},\"references\":[],\"isHosted\":false,\"pillarId\":\"pillar/sport\",\"pillarName\":\"Sport\"}]}}";

        ArrayList<Article> articles = new ArrayList<>();

        try {
            JSONObject rootObject = new JSONObject(jsonTESTString);
            JSONObject response = rootObject.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");


            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentObject = resultsArray.getJSONObject(i);

                String section = currentObject.getString("sectionName");
                String title = currentObject.getString("webTitle");
                String date = currentObject.getString("webPublicationDate");
                String contributor = currentObject.getString("byline");
                String articleUrl = currentObject.getString("webUrl");

                /////////////////////   TEST STRINGS WITH WICH ADAPTER AND LISTVIEW IS OK (TO BE DELETED)////////////////
                /**  String section = "section";
                 String title = "title";
                 String date = "data";
                 String contributor = "contributor";
                 String articleUrl = "url";
                 */

                Article article = new Article(section, title, date, contributor, articleUrl);
                articles.add(article);
            }


        } catch (JSONException e) {

            Log.e("JsonFactory", "Problem parsing the article JSON results", e);
        }


        return articles;
    }

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
}