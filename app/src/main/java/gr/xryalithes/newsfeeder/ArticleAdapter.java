package gr.xryalithes.newsfeeder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Λάμπης on 13/5/2018.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {



    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item, parent, false);
        }
        Article currentArticle = getItem(position);

        String section = currentArticle.getSection();
        String title = currentArticle.getTitle();
        String contributor = currentArticle.getContributor();
        String date = currentArticle.getDatePublished();

//////////////////  TEXT VIEWS DECLARATION/////////////////////////////////////////////////////////////

        TextView sectionTextView =  listItemView.findViewById(R.id.section_text_view);
        sectionTextView.setText(section);

        TextView titleTextView =  listItemView.findViewById(R.id.title_text_view);
        titleTextView.setText(title);

        TextView dateTextView = listItemView.findViewById(R.id.date_text_view);
        dateTextView.setText(date);

        TextView contributorTextView =  listItemView.findViewById(R.id.contributor_text_view);
        contributorTextView.setText(contributor);


        return listItemView;

    }


}


