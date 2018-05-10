package comments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.food.food.food.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<CommentItem> commentList;

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        public TextView content;
        public TextView author;
        public RelativeLayout gradeLayout;
//        public TextView dishName;

        public CommentViewHolder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.dish_comment_card_content);
            author = itemView.findViewById(R.id.dish_comment_card_author);
            gradeLayout = itemView.findViewById(R.id.dish_comment_card_layout);
//            dishName = itemView.findViewById(R.id.dish_comments_dish_name);
        }
    }

    public CommentAdapter(ArrayList<CommentItem> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_comment, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentItem commentItem = commentList.get(position);

        String content = commentItem.getTresc();
        String author = commentItem.getAutor();
        Integer grade = commentItem.getOcena();

        holder.content.setText(content);
        holder.author.setText(author);
//        holder.dishName.setText();
        //Log.d("Grade final", Integer.toString(grade));
        for(int i=0; i<5; i++){
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = i * 150;
            TextView gradeItem = new TextView(holder.gradeLayout.getContext());

            if(i <= grade - 1){
                gradeItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);
            } else {
                gradeItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_border_black_24dp, 0, 0, 0);
            }

            gradeItem.setId(i);
            gradeItem.setLayoutParams(params);
            holder.gradeLayout.addView(gradeItem);
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
