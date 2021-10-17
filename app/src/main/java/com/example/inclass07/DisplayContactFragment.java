package com.example.inclass07;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayContactFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String DATA = null;
    private static final String scheme = "https";
    private static final String host = "www.theappsdr.com";
    private static final String segment = "contacts";
    private static final String segmentd1 = "contact";
    private static final String segmentd = "delete";
    HttpUrl url;
    Request request;
    OkHttpClient client = new OkHttpClient();
    TextView idDisplay;
    TextView nameDisplay;
    TextView emailDisplay;
    TextView phoneDisplay;
    TextView typeDisplay;

    Button update;
    Button cance;
    Button delet;

    // TODO: Rename and change types of parameters
    private Contact mParam1;

    public DisplayContactFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DisplayContactFragment newInstance(Contact param1) {
        DisplayContactFragment fragment = new DisplayContactFragment();
        Bundle args = new Bundle();
        args.putSerializable(DATA, (Serializable) param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Contact) getArguments().getSerializable(DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_contact, container, false);
        getActivity().setTitle(getResources().getString(R.string.view));

        idDisplay = view.findViewById(R.id.textViewDId);
        nameDisplay = view.findViewById(R.id.displayContactName);
        emailDisplay= view.findViewById(R.id.displayEmailAddress);
        phoneDisplay = view.findViewById(R.id.displayPhoneNumber);
        typeDisplay = view.findViewById(R.id.textViewType);

        update = view.findViewById(R.id.buttonUpdate);
        cance = view.findViewById(R.id.buttonAddCancel);
        delet = view.findViewById(R.id.buttonDeleteInDisplay);


//        String[] dataList = mParam1.split(",");

        idDisplay.setText(mParam1.getId());
        nameDisplay.setText(mParam1.getName());
        emailDisplay.setText(mParam1.getEmail());
        phoneDisplay.setText(mParam1.getNumber());
        typeDisplay.setText(mParam1.getType());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.toUpdateScreen(mParam1);
            }
        });

        cance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.navListScreen();
            }
        });

        delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.confirm)
                        .setMessage(R.string.sure)
                        .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                            deleteContactFunction( mParam1.getId() );
                        })
                        .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                        });
                builder.create().show();
            }
        });


        return view;
    }

    DisplayContactFragment.DisplayInterface Listener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof DisplayContactFragment.DisplayInterface) {
            Listener = (DisplayContactFragment.DisplayInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IListener");
        }
    }
    public interface DisplayInterface {
        void toUpdateScreen(Contact d);
        void navListScreen();
    }

    public void deleteContactFunction(String dataId){
        RequestBody form = new FormBody.Builder()
                .add("id", dataId)
                .build();
        HttpUrl.Builder builder = new HttpUrl.Builder();
        url = builder.scheme(scheme)
                .host(host)
                .addPathSegment(segmentd1)
                .addPathSegment(segmentd)
                .build();

        request = new Request.Builder().url(url).post(form).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getResources().getString(R.string.error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Delete Successful", Toast.LENGTH_SHORT).show();

                    }
                });
                Listener.navListScreen();
            }
        });
    }
}