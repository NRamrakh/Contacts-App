package com.example.inclass07;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddContactFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String scheme = "https";
    private static final String host = "www.theappsdr.com";
    private static final String segment = "contact";
    private static final String segmentC = "create";
    EditText nameText;
    EditText numberText;
    EditText emailText;
    RadioGroup radioGroup;
    Button submit;
    Button cancel;
    Request request;
    OkHttpClient client = new OkHttpClient();
    HttpUrl url;
    AddContactFragment.AddInterface Listener;


    int checkedId;
    String type;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddContactFragment() {
        // Required empty public constructor
    }

    public static AddContactFragment newInstance(String param1, String param2) {
        AddContactFragment fragment = new AddContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        getActivity().setTitle(getResources().getString(R.string.add));

        nameText = view.findViewById(R.id.addContactName);
        numberText = view.findViewById(R.id.addPhoneNumber);
        emailText = view.findViewById(R.id.addEmailAddress);
        radioGroup = view.findViewById(R.id.radioGroup);

        submit = view.findViewById(R.id.buttonUpdate);
        cancel = view.findViewById(R.id.buttonAddCancel);

        CharSequence nameForm = (nameText.getText());
        CharSequence num = (numberText.getText());
        CharSequence ema = (emailText.getText());

        checkedId = radioGroup.getCheckedRadioButtonId();
        if(checkedId == R.id.radioButtonHome){
            type = "HOME";
        } else if(checkedId == R.id.radioButtonCell) {
            type = "CELL";
        } else if(checkedId == R.id.radioButtonOffice){
            type = "OFFICE";
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameForm.toString().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.NameError), Toast.LENGTH_SHORT).show();
                } else if (num.toString().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.NumberFieldError), Toast.LENGTH_SHORT).show();
                } else if (ema.toString().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.EmailrFieldError), Toast.LENGTH_SHORT).show();
                } else {
                    if ((num.toString()).replace("-","").length() == 10) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.confirm)
                                .setMessage(R.string.sure)
                                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                                    createContact(nameForm.toString(), num.toString(), type, ema.toString());
                                })
                                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {});
                        builder.create().show();

                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.NumberError), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.toListScreen();
            }
        });


        return view;
    }

    void createContact(String name, String number, String type, String email) {
        RequestBody form = new FormBody.Builder()
                .add("name", name)
                .add("phone", number)
                .add("type", type)
                .add("email", email)
                .build();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        url = builder.scheme(scheme)
                .host(host)
                .addPathSegment(segment)
                .addPathSegment(segmentC)
                .build();

        request = new Request.Builder().url(url).post(form).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();

                        }
                    });
                    Listener.toListScreen();
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddContactFragment.AddInterface) {
            Listener = (AddContactFragment.AddInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IListener");
        }
    }

    public interface AddInterface {
//        void toListScreen(Boolean flag);
        void toListScreen();
    }


}