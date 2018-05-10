package profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.food.food.food.R;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView profileName = view.findViewById(R.id.profile_name);
        TextView profileAddress = view.findViewById(R.id.profile_address);

        Button editData = view.findViewById(R.id.profile_edit_data);

        //if shared preferences type == user or type == restaurant

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        String typeFromPreferences = sharedPref.getString("type", "");
        String nazwaFromPreferences = sharedPref.getString(getString(R.string.nazwa), "");
        String adresFromPreferences = sharedPref.getString("adres", "");
        if(typeFromPreferences.equals("restaurant")){

            Button addDish = view.findViewById(R.id.profile_add_dish);
            Log.d("addDish", addDish.toString());
            addDish.setVisibility(View.VISIBLE);

            addDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileAddDishFragment()).addToBackStack(null).commit();
                }
            });
        }

        if(!nazwaFromPreferences.equals("") && !typeFromPreferences.equals("") && !adresFromPreferences.equals("")){
            String fullName = "Nazwa: " + nazwaFromPreferences;
            String fullAddress = "Adres: " + adresFromPreferences;
            profileName.setText(fullName);
            profileAddress.setText(fullAddress);
        }

        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileEditFragment()).addToBackStack(null).commit();
            }
        });

        return view;
    }
}
