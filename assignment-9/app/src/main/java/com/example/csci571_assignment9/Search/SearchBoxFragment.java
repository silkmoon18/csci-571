package com.example.csci571_assignment9.Search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.csci571_assignment9.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchBoxFragment extends Fragment {

    static final String IPINFO_URL = "https://ipinfo.io/?token=ddf760ed1e2c25";

    RequestQueue requestQueue;
    String urlRoot;

    AutoCompleteTextView inputKeyword;
    EditText inputDistance;
    Spinner inputCategory;
    EditText inputLocation;
    CheckBox inputAutoLocation;
    Button buttonSubmit;
    Button buttonClear;

    ArrayList<String> autocompleteList;
    String[] detectedLocation = new String[0];
    String[] categories = {"All", "arts, All", "health, All", "hotelstravel, All", "food, All", "professional, All"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(requireActivity());
        urlRoot = getResources().getString(R.string.url_root);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_box, container, false);

        inputKeyword = v.findViewById(R.id.input_search_keyword);
        inputDistance = v.findViewById(R.id.input_search_distance);
        inputCategory = v.findViewById(R.id.spinner_search_category);
        inputLocation = v.findViewById(R.id.input_search_location);
        inputAutoLocation = v.findViewById(R.id.checkbox_search_autoLocation);
        buttonSubmit = v.findViewById(R.id.button_search_submit);
        buttonClear = v.findViewById(R.id.button_search_clear);

        initKeywordAutocomplete();
        initCategorySpinner();
        initAutoLocation();
        initButtons();

        return v;
    }

    private void initKeywordAutocomplete() {
        inputKeyword.setThreshold(1);
        inputKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                updateAutocompleteList(editable.toString());
            }
        });
    }

    private void initCategorySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_search_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        inputCategory.setAdapter(adapter);
    }

    private void initAutoLocation() {
        inputAutoLocation.setOnCheckedChangeListener((buttonView, isChecked)
                -> onAutoLocationUpdate(isChecked));
    }

    private void initButtons() {
        buttonSubmit.setOnClickListener(v -> onSubmit());
        buttonClear.setOnClickListener(v -> onClear());
    }

    private void updateAutocompleteList(String keyword) {
        inputKeyword.setAdapter(new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_list_item_activated_1, autocompleteList));

        if (keyword.length() == 0)
            return;

        String url = urlRoot + "/handle_autocomplete?keyword=" + keyword;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        autocompleteList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            autocompleteList.add(jsonArray.getString(i));
                        }
                        if (autocompleteList.size() == 0) {
                            autocompleteList.add("");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(),
                                android.R.layout.simple_list_item_activated_1, autocompleteList);
                        inputKeyword.setAdapter(adapter);
                        inputKeyword.showDropDown();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("autocomplete", error.toString()));

        requestQueue.add(stringRequest);
    }

    private void onAutoLocationUpdate(boolean enabled) {
        if (enabled) {
            inputLocation.setVisibility(View.INVISIBLE);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, IPINFO_URL,
                    response -> {
                        try {
                            JSONObject res = new JSONObject(response);
                            detectedLocation = res.getString("loc").split(",");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Log.e("autocomplete", error.toString()));

            requestQueue.add(stringRequest);
        }
        else {
            inputLocation.setVisibility(View.VISIBLE);
            detectedLocation = new String[0];
        }
    }

    private boolean checkInputs() {
        String error = "This field is required";
        boolean result = true;
        if (inputKeyword.getText().toString().length() == 0) {
            inputKeyword.setError(error);
            result = false;
        }
        if (inputDistance.getText().toString().length() == 0) {
            inputDistance.setError(error);
            result = false;
        }
        if (!inputAutoLocation.isChecked() && inputLocation.getText().toString().length() == 0) {
            inputLocation.setError(error);
            result = false;
        }
        return result;
    }

    private void onSubmit() {
        if (!checkInputs())
            return;

        JSONObject params = new JSONObject();
        try {
            params.put("keyword", inputKeyword.getText().toString());
            params.put("distance", inputDistance.getText().toString());
            params.put("category", categories[inputCategory.getSelectedItemPosition()]);
            params.put("location", inputLocation.getText().toString());
            if (detectedLocation.length == 2)
                params.put("detectedLocation", new JSONArray(detectedLocation));
            else
                params.put("detectedLocation", "");

            params.put("autoLocated", inputAutoLocation.isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = urlRoot + "/handle_input?params=" + params;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray res = new JSONArray(response);
                        ((MainActivity)requireActivity()).displayResult(res);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        try {
                            ((MainActivity)requireActivity()).displayResult(new JSONArray());
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                },
                error -> {
                    Log.e("onSubmit()", error.toString());
                    try {
                        ((MainActivity)requireActivity()).displayResult(new JSONArray());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void onClear() {
        inputKeyword.setText("");
        inputDistance.setText("");
        inputCategory.setSelection(0);
        inputLocation.setText("");
        inputAutoLocation.setChecked(false);
        ((MainActivity)requireActivity()).clearResult();

        inputKeyword.setError(null);
        inputDistance.setError(null);
        inputLocation.setError(null);
    }
}