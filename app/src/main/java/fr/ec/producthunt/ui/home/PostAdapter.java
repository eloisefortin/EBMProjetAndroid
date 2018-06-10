package fr.ec.producthunt.ui.home;

import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Post;

import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class PostAdapter extends BaseAdapter {

    private List<Post> dataSource = Collections.emptyList();
    private Boolean firstPost;
    public PostAdapter() {
        firstPost = true;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolderSmall;
        ViewHolder viewHolderBig;

        Post post = dataSource.get(position);

        Log.d(TAG, "getView: position : " + position);

        if (position == 0 && firstPost) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_big, parent, false);

            viewHolderBig = new ViewHolder();
            viewHolderBig.title = convertView.findViewById(R.id.title);
            viewHolderBig.subTitle = convertView.findViewById(R.id.sub_title);
            viewHolderBig.postImage = convertView.findViewById(R.id.img_product);
            viewHolderBig.nbComments = convertView.findViewById(R.id.comments);

            convertView.setTag(viewHolderBig);


            viewHolderBig.title.setText(post.getTitle());
            viewHolderBig.subTitle.setText(post.getSubTitle());
            viewHolderBig.nbComments.setText(post.getNbComments()+" commentaires");
            Picasso.with(parent.getContext())
                    .load(post.getImageUrl())
                    .centerCrop()
                    .fit()
                    .into(viewHolderBig.postImage);
            firstPost=false;
        } else {
            //set viewHolderSmall
            if (convertView == null || position == 1) {
                convertView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item, parent, false);

                viewHolderSmall = new ViewHolder();
                viewHolderSmall.title = convertView.findViewById(R.id.title);
                viewHolderSmall.subTitle = convertView.findViewById(R.id.sub_title);
                viewHolderSmall.postImage = convertView.findViewById(R.id.img_product);
                viewHolderSmall.nbComments = convertView.findViewById(R.id.comments);
                convertView.setTag(viewHolderSmall);
            } else {

                viewHolderSmall = (ViewHolder) convertView.getTag();
            }
            viewHolderSmall.title.setText(post.getTitle());
            viewHolderSmall.subTitle.setText(post.getSubTitle());
            viewHolderSmall.nbComments.setText(post.getNbComments()+" commentaires");
            Picasso.with(parent.getContext())
                    .load(post.getImageUrl())
                    .centerCrop()
                    .fit()
                    .into(viewHolderSmall.postImage);
        }


        return convertView;
    }

    public void showPosts(List<Post> posts) {
        dataSource = posts;

        notifyDataSetChanged();
    }


    private static class ViewHolder {
        TextView title;
        TextView subTitle;
        ImageView postImage;
        TextView nbComments;
    }
}
