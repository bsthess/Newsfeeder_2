package gr.xryalithes.newsfeeder;

/**
 * Created by Λάμπης on 29/5/2018.
 */
//////////////////THE CLASS WHICH HOLDS AND SERVES   THE OBJECT VARIABLES FOR THE ARTICLE///////////////////////////////////
public class Article {

    private String mContributor;
    private String mTitle;
    private String mDatePublished;
    private String mSection;
    private String mArticleUrl;
    private String mImage;

    public Article(String section, String title, String datePublished, String contributor, String articleUrl, String image) {
        mContributor = contributor;
        mTitle = title;
        mDatePublished = datePublished;
        mSection = section;
        mArticleUrl = articleUrl;
        mImage = image;
    }

    public String getImage() {
        return mImage;
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

    public String getUrl() {
        return mArticleUrl;
    }

}

