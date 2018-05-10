package com.food.food.food;

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
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

import comments.AddCommentDialog;

public class DishFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private DishAdapter mAdapter;
    private ArrayList<DishItem> dishItems;
    private RecyclerView.LayoutManager mLayoutManager;
    private RequestQueue Queue;

    private Button addCommentsButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_dish, container, false);

        mRecyclerView = view.findViewById(R.id.dish_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        dishItems = new ArrayList<>();

        final Bundle bundle = getArguments();
        if(bundle != null){
            String dishes = bundle.getString("dishes");
            Log.d("DISHES", dishes);
//            try {
            //Bundle stores restaurant id
                String url = "https://food--api.herokuapp.com/dania/" + bundle.getString("id") + "/" + bundle.getString("dishType");
                //Fetch comments only? Just in case anything updated
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("RESPONSE", response.toString());
                        try {

                            for(int i=0;i<response.length(); i++){
                                JSONObject dish = response.getJSONObject(i);
                                JSONArray commentsResponse = dish.getJSONArray("komentarze");

                                Double averageDivided = 0.0;

                                if(commentsResponse.length() != 0){
                                    Integer averageGrade = 0;
                                    //Get the grade
                                    for(int j=0;j<commentsResponse.length(); j++){
                                        JSONObject singleComment = commentsResponse.getJSONObject(j);
                                        Integer grade = Integer.parseInt(singleComment.getString("ocena"));
                                        averageGrade += grade;
                                    }
                                    averageDivided = (double)averageGrade / commentsResponse.length();

                                }

                                dishItems.add(new DishItem(
                                        dish.getString("nazwa"),
                                        dish.getString("cena"),
                                        dish.getString("czas"),
                                        dish.getString("img"),
                                        dish.getString("_id"),
                                        bundle.getString("id"),
                                        averageDivided,
                                        dish.getJSONArray("komentarze")
                                ));
                            }

                            mRecyclerView = view.findViewById(R.id.dish_list);
                            mLayoutManager = new LinearLayoutManager(getContext());
                            mAdapter = new DishAdapter(dishItems);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(mAdapter);
                        } catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                Queue.add(request);

                //Old code for getting data from bundle passed by RestaurantItemDetailFragment

//                JSONArray dishesArray = new JSONArray(dishes);
//                for(int i=0;i<dishesArray.length(); i++){
//                    JSONObject dish = dishesArray.getJSONObject(i);
//
//                    dishItems.add(new DishItem(
//                            dish.getString("nazwa"),
//                            dish.getString("cena"),
//                            dish.getString("czas"),
//                            dish.getString("img"),
//                            dish.getString("_id"),
//                            dish.getJSONArray("komentarze")
//                    ));
//                }

//                mRecyclerView = view.findViewById(R.id.dish_list);
//                mLayoutManager = new LinearLayoutManager(getContext());
//                mAdapter = new DishAdapter(dishItems);
//                mRecyclerView.setLayoutManager(mLayoutManager);
//                mRecyclerView.setAdapter(mAdapter);

//            } catch (JSONException e){
//                e.printStackTrace();
//            }
        }

        return view;
    }


}
