package com.example.inclass07;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ListContactFragment extends Fragment implements ContactListViewAdapter.ContactListInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String scheme = "https";
    private static final String host = "www.theappsdr.com";
    private static final String segment = "contacts";

    private static final String segmentd1 = "contact";
    private static final String segmentd = "delete";
    HttpUrl url;
    Request request;
    OkHttpClient client = new OkHttpClient();


    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ContactListViewAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListContactFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ListContactFragment newInstance(String param1, String param2) {
        ListContactFragment fragment = new ListContactFragment();
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
        View view = inflater.inflate(R.layout.fragment_list_contact, container, false);
        getActivity().setTitle(getResources().getString(R.string.list));


        recyclerView = view.findViewById(R.id.recyclerViewListContact);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.toAddScreen();
            }
        });

        getContactsAndSet();

        return view;
    }

    void getContactsAndSet() {
        HttpUrl.Builder builder = new HttpUrl.Builder();
        url = builder.scheme(scheme)
                .host(host)
                .addPathSegment(segment)
                .build();

        request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), getResources().getString(R.string.error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody responseBody = response.body();
                    String body = responseBody.string();
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (body != "") {
                        mainActivity.reloadContacts(body);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new ContactListViewAdapter(mainActivity.contacts, ListContactFragment.this);
                                recyclerView.setAdapter(adapter);
                            }
                        });
                    } else {
                        mainActivity.contacts.clear();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new ContactListViewAdapter(mainActivity.contacts, ListContactFragment.this);
                                recyclerView.setAdapter(adapter);
                                Toast.makeText(getContext(), getResources().getString(R.string.contact_error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
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


    ListInterface Listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ListContactFragment.ListInterface) {
            Listener = (ListContactFragment.ListInterface) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement IListener");
        }
    }

    @Override
    public void toViewScreenFrag(Contact data) {
        Listener.toViewScreen(data);
    }

    @Override
    public void deleteContact(String d) {
        RequestBody form = new FormBody.Builder()
                .add("id", d)
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
                        Toast.makeText(getContext(), getResources().getString(R.string.deleteMsg), Toast.LENGTH_SHORT).show();

                    }
                });
//                Listener.navListScreen();
                getContactsAndSet();
            }
        });
//    }
    }

    public interface ListInterface {
        void toAddScreen();
        void toViewScreen(Contact data);
    }

}