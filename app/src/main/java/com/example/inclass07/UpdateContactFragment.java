package com.example.inclass07;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateContactFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String data = "data";

    // TODO: Rename and change types of parameters
    private Contact mParam1;

    public UpdateContactFragment() {
        // Required empty public constructor
    }

    TextView idTv;
    EditText nameET;
    EditText emailET;
    EditText phoneET;
    RadioButton homeRB;
    RadioButton officeRB;
    RadioButton cellRB;

    Button update;
    Button cance;

    int checkedId;
    RadioGroup radioGroupUpd;
    HttpUrl url;
    Request request;
    OkHttpClient client = new OkHttpClient();
    private static final String scheme = "https";
    private static final String host = "www.theappsdr.com";
    private static final String segment = "contact";
    private static final String segmentU = "update";

    // TODO: Rename and change types and number of parameters
    public static UpdateContactFragment newInstance(Contact param1) {
        UpdateContactFragment fragment = new UpdateContactFragment();
        Bundle args = new Bundle();
        args.putSerializable(data, (Serializable) param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Contact) getArguments().getSerializable(data);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_contact, container, false);
        getActivity().setTitle(getResources().getString(R.string.upd));

        idTv = view.findViewById(R.id.textViewUpId);
        nameET = view.findViewById(R.id.updateContactName);
        emailET = view.findViewById(R.id.updateEmailAddress);
        phoneET = view.findViewById(R.id.updatePhoneNumber);
        radioGroupUpd = view.findViewById(R.id.radioGroupUpd);

        update = view.findViewById(R.id.buttonUpdateUPD);
        cance = view.findViewById(R.id.buttonAddCancelUPD);

        homeRB = view.findViewById(R.id.radioButtonHomeUpd);
        cellRB = view.findViewById(R.id.radioButtonCellUpd);


        idTv.setText(mParam1.getId());
        nameET.setText(mParam1.getName());
        emailET.setText(mParam1.getEmail());
//        phoneET.setText(dataList[3].replace("-",""));
        phoneET.setText(mParam1.getNumber());
        if (mParam1.getType().equals("HOME")) {
            radioGroupUpd.check(R.id.radioButtonHomeUpd);
        } else if (mParam1.getType().equals("CELL")) {
            radioGroupUpd.check(R.id.radioButtonCellUpd);
        } else {
            radioGroupUpd.check(R.id.radioButtonOfficeUpd);
        }

        cance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.backToList();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence nameForm = (nameET.getText());
                CharSequence num = (phoneET.getText());
                CharSequence ema = (emailET.getText());
                String type;
                checkedId = radioGroupUpd.getCheckedRadioButtonId();
                if (checkedId == R.id.radioButtonHomeUpd) {
                    type = "HOME";
                } else if (checkedId == R.id.radioButtonCellUpd) {
                    type = "CELL";
                } else {
                    type = "OFFICE";
                }

                if (nameForm.toString().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.NameError), Toast.LENGTH_SHORT).show();
                } else if (num.toString().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.NumberFieldError), Toast.LENGTH_SHORT).show();
                } else if (ema.toString().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.EmailrFieldError), Toast.LENGTH_SHORT).show();
                } else {
                    if ((num.toString()).replace("-", "").length() == 10) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.confirm)
                                .setMessage(R.string.sure)
                                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                                    updateContact(nameForm.toString(), num.toString(), type, ema.toString(), mParam1.getId());
                                })
                                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                                });
                        builder.create().show();

                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.NumberError), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    void updateContact(String name, String number, String type, String email, String id) {
        RequestBody form = new FormBody.Builder()
                .add("id", id)
                .add("name", name)
                .add("phone", number)
                .add("type", type)
                .add("email", email)
                .build();

        HttpUrl.Builder builder = new HttpUrl.Builder();
        url = builder.scheme(scheme)
                .host(host)
                .addPathSegment(segment)
                .addPathSegment(segmentU)
                .build();

        request = new Request.Builder().url(url).post(form).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getResources().getString(R.string.error) + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("demo", "onResponse: " + response);
                if (response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), getResources().getString(R.string.updateMsg), Toast.LENGTH_SHORT).show();
                        }
                    });
//                    Listener.backToList();
                    String str = id + "," + name + "," + email + "," + number + "," + type;
                    Contact s = new Contact(str);
                    Listener.detailPage(s);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), getResources().getString(R.string.common_error), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    UpdateContactFragment.UpdateInterface Listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UpdateContactFragment.UpdateInterface) {
            Listener = (UpdateContactFragment.UpdateInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IListener");
        }
    }

    public interface UpdateInterface {
        void backToList();

        void detailPage(Contact s);
    }
}