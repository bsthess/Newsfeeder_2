package gr.xryalithes.newsfeeder;

/**
 * Created by Λάμπης on 29/5/2018.
 */

public class Article {

    private String mContributor;
    private String mTitle;
    private String mDatePublished;
    private String mSection;
    private String mArticleUrl;

    public Article(String contributor, String title, String datePublished, String section,String articleUrl) {
        mContributor = contributor;
        mTitle = title;
        mDatePublished = datePublished;
        mSection = section;
        mArticleUrl = articleUrl;
    }

    public String getContributor() {

        return mContributor;
    }

    public String getTitle() {

        return mTitle;
    }

    public String getDatePublished() {
        return mDatePublished;
    }

    public String getSection() {
        return mSection;

    }
    public String getUrl(){
        return mArticleUrl;
    }

}

