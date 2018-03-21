package fr.ec.producthunt.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import fr.ec.producthunt.R;
import fr.ec.producthunt.data.model.Collection;

/**
 * Created by hazegard on 21/03/18.
 */

public class CollectionAdapter extends BaseAdapter {

  private List<Collection> dataSource = Collections.emptyList();

  public CollectionAdapter() {
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
    ViewHolder viewHolder;

    if (convertView == null) {
      convertView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item, parent, false);

      viewHolder = new ViewHolder();
      viewHolder.title = convertView.findViewById(R.id.title);
      viewHolder.name = convertView.findViewById(R.id.sub_title);
      viewHolder.image = convertView.findViewById(R.id.img_product);

      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    Collection collection = dataSource.get(position);

    viewHolder.name.setText(collection.getName());
    viewHolder.title.setText(collection.getTitle());

    Picasso.with(parent.getContext())
      .load(collection.getBackground_image_url())
      .centerCrop()
      .fit()
      .into(viewHolder.image);
    return convertView;
  }

  public void showCollections(List<Collection> collections) {
    dataSource = collections;
    notifyDataSetChanged();
  }

  private static class ViewHolder {
    TextView title;
    TextView name;
    ImageView image;
  }

}
