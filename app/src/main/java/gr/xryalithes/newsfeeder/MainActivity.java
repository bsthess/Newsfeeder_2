package gr.xryalithes.newsfeeder;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.net.ConnectivityManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {
    private static final String URL_TO_JSON = "https://content.guardianapis.com/search";
    private static final String URL_TEST = "https://content.guardianapis.com/search?section=football&order-by=newest&show-elements=image&show-fields=byline%2Cthumbnail&page=1&page-size=10&api-key=test";
    private static final int ARTICLE_LOADER_ID = 1;
    private ArticleAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /////////////// CREATE A LOADER MANAGER AND INITIALIZE A LOADER///////////////////////////////
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);

        //////////////DECLARE A LISTVIEW AND SETTING THE EMPTYSTATE OF IT////////////////////////
        ListView articleListView = findViewById(R.id.list);
        mEmptyStateTextView = findViewById(R.id.empty);
        articleListView.setEmptyView(mEmptyStateTextView);

        ////////////CREATE AN ARTICLE ADAPTER THAT HOLDS ARTICLE OBJECTS AND CONNECT IT TO LISTVIEW////////////////////
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        articleListView.setAdapter(mAdapter);

        //////////WHEN CLICK, OPEN THE ARTICLE WEBSITE////////////////////////////////////////
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentArticle = mAdapter.getItem(position);
                Uri articleUri = Uri.parse(currentArticle.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                startActivity(websiteIntent);
            }
        });
    }

    //////////////CHECKING IF THE DEVICE IS CONNECTED TO INTERNET AND RETURN A BOOLEAN VALUE////////////////////////////////
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        //  4 strings,one for each preference.
        String keyword = sharedPrefs.getString(
                getString(R.string.settings_keyword_key),
                getString(R.string.settings_keyword_default));
        String sectionSelected = sharedPrefs.getString(
                getString(R.string.settings_SECTION_key),
                getString(R.string.settings_section_default));
        Log.v("SECTIONSELECTED=", sectionSelected);
        String orderBySelected = sharedPrefs.getString(
                getString(R.string.settings_orderBy_key),
                getString(R.string.settings_orderBy_default));
        String pageResults = sharedPrefs.getString(
                getString(R.string.settings_page_results_key),
                getString(R.string.settings_page_results_default));
        Log.v("PAGERSULTS IS: ", pageResults);
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(URL_TO_JSON);
        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //// /////////CREATING THE QUERY PARAMETERS....FIRST VALUE IS THE PARAMETER,SECOND THE VALUE SELECTED///////////////////////////////
        ///THE LOGIC: IF USER DOES NOT INSERT A KEYWORD, THEN DON'T APPEND THIS PARAMETER TO THE QUERY URL
        if (sectionSelected != getString((R.string.settings_section_all_value))) {
            uriBuilder.appendQueryParameter("section", sectionSelected);
        }
        uriBuilder.appendQueryParameter("order-by", orderBySelected);
        uriBuilder.appendQueryParameter("show-elements", "image");
        uriBuilder.appendQueryParameter("show-fields", "byline,thumbnail");
        uriBuilder.appendQueryParameter("page", "1");
        uriBuilder.appendQueryParameter("page-size", pageResults);
        uriBuilder.appendQueryParameter("q", keyword);
        uriBuilder.appendQueryParameter("api-key", "test");
        String urlFinal = uriBuilder.toString();// THE FINAL COMPLETE URL
        Log.v("url is :", urlFinal);//THIS IS JUST TO CHECK THE URL CREATED, FOR DEBUGGING
        return new ArticleLoader(this, urlFinal);
    }
    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        mAdapter.clear();
        View loadingIndicator = findViewById(R.id.circle_indicator);
        loadingIndicator.setVisibility(View.GONE);
/////////// IF WE HAVE DATA, ADD THEM TO LIST.IF THERE IS NO INTERNET OR DATA,SHOW MESSAGES///////////////////////
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        } else {
            if (!isConnected()) {
                mEmptyStateTextView.setText(R.string.no_internet);
            } else {
                mEmptyStateTextView.setText(R.string.no_news);
            }
        }
    }
    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

        mAdapter.clear();
    }

    @Override
    // This method initialize the contents of the Activity's options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // This method is called whenever an item in the options menu is selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
