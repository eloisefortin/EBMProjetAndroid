package fr.ec.producthunt.ui.comments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.ec.producthunt.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CommentsActivityFragment extends Fragment {

    public CommentsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }
}
