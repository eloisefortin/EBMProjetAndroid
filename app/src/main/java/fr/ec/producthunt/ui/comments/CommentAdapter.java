package fr.ec.producthunt.ui.comments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Comment;

/**
 * Created by erwan on 10/06/2018.
 */

public class CommentAdapter extends BaseAdapter{

    private List<Comment> dataSource = Collections.emptyList();

    public CommentAdapter() {
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
        CommentAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_item, parent, false);

            viewHolder = new CommentAdapter.ViewHolder();
            viewHolder.user = convertView.findViewById(R.id.user);
            viewHolder.body = convertView.findViewById(R.id.body);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommentAdapter.ViewHolder) convertView.getTag();
        }

        Comment comment = dataSource.get(position);

        viewHolder.user.setText(comment.getUser()+"   "+comment.getUsername());
        viewHolder.body.setText(comment.getBody());

        return convertView;
    }

    public void showComments(List<Comment> comments) {
        dataSource = comments;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView user;
        TextView body;
    }

}
