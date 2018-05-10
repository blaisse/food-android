package comments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.food.food.food.DishAdapter;
import com.food.food.food.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CommentAdapter mAdapter;
    private ArrayList<CommentItem> commentItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        mRecyclerView = view.findViewById(R.id.dish_comments_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        TextView dishName = view.findViewById(R.id.dish_comments_dish_name);
//        RelativeLayout gradeLayout = view.findViewById(R.id.dish_comment_card_layout);

        commentItems = new ArrayList<>();

        Bundle bundle = getArguments();
        if(bundle != null){
            String commentsString = bundle.getString("comments");
            try {
                JSONArray comments = new JSONArray(commentsString);
                for(int i=0; i<comments.length(); i++){
                    JSONObject comment = comments.getJSONObject(i);
                    JSONObject author = comment.getJSONObject("autor");

                    commentItems.add(new CommentItem(comment.getString("tresc"), author.getString("nazwa"), Integer.parseInt(comment.getString("ocena"))));
                }

                dishName.setText(bundle.getString("dishName"));

                mRecyclerView = view.findViewById(R.id.dish_comments_list);
                mLayoutManager = new LinearLayoutManager(getContext());
                mAdapter = new CommentAdapter(commentItems);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

            } catch(JSONException e){
                e.printStackTrace();
            }
        }

        return view;
    }
}
