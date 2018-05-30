package gr.xryalithes.newsfeeder;


import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {


    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String URL_TO_JSON = "https://content.guardianapis.com/search?section=football&order-by=newest&use-date=published&show-references=author&show-fields=byline&page=1&page-size=10&api-key=test";
    private static final int ARTICLE_LOADER_ID = 1;
    private ArticleAdapter mAdapter;
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoaderManager loaderManager = getLoaderManager();


        loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);


        ListView articleListView = findViewById(R.id.list);
        mEmptyStateTextView = findViewById(R.id.empty);
        articleListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());


        articleListView.setAdapter(mAdapter);

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

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        return new ArticleLoader(this, URL_TO_JSON);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {

        mAdapter.clear();
        View loadingIndicator = findViewById(R.id.circle_indicator);
        loadingIndicator.setVisibility(View.GONE);


        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
        mEmptyStateTextView.setText(R.string.no_news);
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

        mAdapter.clear();
    }

}
