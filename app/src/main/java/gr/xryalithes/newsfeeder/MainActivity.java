package gr.xryalithes.newsfeeder;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.net.ConnectivityManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {


    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String URL_TO_JSON = "https://content.guardianapis.com/search?section=football&order-by=newest&show-elements=image&show-fields=byline%2Cthumbnail&page=1&page-size=10&api-key=test";
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
////////////
    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        return new ArticleLoader(this, URL_TO_JSON);
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

}
