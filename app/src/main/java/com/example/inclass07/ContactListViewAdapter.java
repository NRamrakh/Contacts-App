package com.example.inclass07;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListViewAdapter extends RecyclerView.Adapter<ContactListViewAdapter.ListViewHolder> {
    ArrayList<Contact> Lists;


    ContactListInterface contactListInterface;

    public ContactListViewAdapter(ArrayList<Contact> data, ContactListInterface contactListInterface) {
        this.Lists = data;
        this.contactListInterface = contactListInterface;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contact_layout, parent, false);
        ContactListViewAdapter.ListViewHolder viewHolder = new ContactListViewAdapter.ListViewHolder(view, contactListInterface);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Contact SelectedContact = Lists.get(position);
        holder.contactId.setText(SelectedContact.getId());
        if (SelectedContact.getName().length() > 20) {
            holder.name.setText(SelectedContact.getName().substring(0, 20));
        } else {
            holder.name.setText(SelectedContact.getName());
        }
        holder.email.setText(SelectedContact.getEmail());
        holder.number.setText(SelectedContact.getNumber());
        holder.contactType.setText(SelectedContact.getType());
        holder.data = SelectedContact;
        holder.id = SelectedContact.getId();

    }

    @Override
    public int getItemCount() {
        return Lists.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {
        TextView name, number, email, contactId, contactType;
        String id;
        Contact data;
        ContactListInterface contactListInterface;
        Button deleteButton;

        public ListViewHolder(@NonNull View itemView, ContactListInterface contactListInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            number = itemView.findViewById(R.id.textViewContactNumber);
            contactId = itemView.findViewById(R.id.textViewID);
            email = itemView.findViewById(R.id.textViewEmail);
            contactType = itemView.findViewById(R.id.textViewContactType);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactListInterface.deleteContact(id);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contactListInterface.toViewScreenFrag(data);
                }
            });
        }
    }

    interface ContactListInterface {
        void toViewScreenFrag(Contact data);

        void deleteContact(String d);
    }


}
