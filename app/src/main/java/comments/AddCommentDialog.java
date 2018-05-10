package comments;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.food.food.food.HomeFragment;
import com.food.food.food.LoginFragment;
import com.food.food.food.R;
import com.food.food.food.RestaurantListFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddCommentDialog extends AppCompatDialogFragment {

//    private Map<String, String> params;
    private RequestQueue Queue;
    private Activity activity;

    private EditText editAddComment;
    private TextView gradeView1;
    private TextView gradeView2;
    private Integer gradeIndex;

//    private AddCommentDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        activity = getActivity();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_comment, null);

        RelativeLayout layout = view.findViewById(R.id.dialog_add_comment_layout);

        editAddComment = view.findViewById(R.id.edit_dish_add_comment);
//        gradeView1 = view.findViewById(R.id.edit_dish_add_grade1);
//        gradeView2 = view.findViewById(R.id.edit_dish_add_grade2);

        for(int i=0; i<5; i++){

            final int index = i;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = i * 180;
            final TextView gradeItem = new TextView(getContext());
            gradeItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_border_black_24dp, 0, 0, 0);
            gradeItem.setId( + i);
//            gradeItem.setLeft(200);
            gradeItem.setLayoutParams(params);
            layout.addView(gradeItem);

            //Set on click
            //How to center this - made another relative layout
            gradeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Set the grade index
                    gradeIndex = index;
                    //Clear all stars
                    for(int i=0; i<5; i++){
                        String id = Integer.toString(i);
                        int resId = getResources().getIdentifier(id, "id", getActivity().getPackageName());
                        TextView text = view.findViewById(resId);
                        text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_border_black_24dp, 0, 0,0);
                    }

                    //Integer.toString(gradeItem.getId())
                    gradeItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);
                    if(index > 0){
                        for(int i=index-1;i>=0;i--){
//                            String id = "R.id" + i;
                            String id = Integer.toString(i);
                            int resId = getResources().getIdentifier(id, "id", getActivity().getPackageName());

                            //Had to use outer view instead of v
                            TextView text = view.findViewById(resId);
                            text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0,0);
                        }
                    }
                }
            });

        }

//        gradeView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("TextView", Integer.toString(v.getId()));
//                gradeView1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);
//            }
//        });
//
//        gradeView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("TextView", Integer.toString(v.getId()));
//            }
//        });

//        InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.showSoftInput(editAddComment, InputMethodManager.SHOW_IMPLICIT);
//        editAddComment.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

        builder.setView(view).setTitle("Dodaj komentarz").setNegativeButton("Powrót", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Hide keyboard on cancel
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        }).setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                String content = editAddComment.getText().toString();

                if(gradeIndex != null && content.length() != 0){

//                String grade = gradeView.getText().toString();

                    Queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                    //Hide keyboard on ok
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    //Send comment to DishAdapter
//                listener.applyData(content);

                    final Bundle bundle = getArguments();

                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    final String tokenFromPreferences = sharedPref.getString(getString(R.string.token), "");
                    String typeFromPreference = sharedPref.getString("type", "");

                    Map<String, String> params = new HashMap<>();

                    params.put("type", typeFromPreference);
                    params.put("dish", bundle.getString("dishId"));
                    params.put("tresc", content);
                    params.put("ocena", Integer.toString(gradeIndex+1));//+1

                    JSONObject parameters = new JSONObject(params);

                    final Activity act = getActivity();
                    final android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
//                fm.beginTransaction()

                    //Save to DB
                    String url = "https://food--api.herokuapp.com/komentarz";
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());

                            String commentsString = bundle.getString("comments");
                            ArrayList<JSONObject> commentsArray = new ArrayList<>();
                            try {

                                commentsArray.add(response);
                                JSONArray comments = new JSONArray(commentsString);
                                for(int i=0; i<comments.length(); i++){
                                    commentsArray.add(comments.getJSONObject(i));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                            CommentFragment commentFragment = new CommentFragment();
//                        RestaurantListFragment listFragment = new RestaurantListFragment();
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("comments", commentsArray.toString());
                            bundle2.putString("dishName", bundle.getString("dishName"));

                            commentFragment.setArguments(bundle2);
//                        listFragment.setArguments(bundle2);
//                        RestaurantListFragment restList = (RestaurantListFragment) fm.findFragmentById(R.id.rest_list);
//                        fm.beginTransaction().detach(restList);
//                        fm.beginTransaction().attach(restList);

                            fm.beginTransaction().replace(R.id.fragment_container, commentFragment).addToBackStack(null).commit();
//                        AddCommentDialog.this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, commentFragment).addToBackStack(null).commit();
//                        ().beginTransaction().replace(R.id.fragment_container, commentFragment).commit();
//                        getParentFragment().getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, new CommentFragment()).commit();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("auth", tokenFromPreferences);
                            return params;
                        }
                    };

                    Queue.add(request);

                } else {
                    //Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    Toast.makeText(getContext(), "Wszystkie pola są wymagane", Toast.LENGTH_SHORT).show();
                }


            }
        });
        return builder.create();
    }


    //    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            listener = (AddCommentDialogListener) getParentFragment();
////            listener = (AddCommentDialogListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString());
//        }
//    }
//
//    public interface AddCommentDialogListener {
//        void applyData(String content);
//    }
}
