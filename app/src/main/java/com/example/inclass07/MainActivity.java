/*
 Assignment: In Class Assignment Number 7
 File Name: MainActivity.java
 Group Number 19
 Full Name:
 Nisha Ramrakhyani
 Punit Mashruwala
*/
package com.example.inclass07;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ListContactFragment.ListInterface, AddContactFragment.AddInterface, DisplayContactFragment.DisplayInterface, UpdateContactFragment.UpdateInterface {
    public Boolean flag = false;

    public static ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toListScreen();
    }

    @Override
    public void toAddScreen() {
//        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new AddContactFragment(), "addContact")
                .addToBackStack("addContact")
                .commit();

    }

    @Override
    public void toViewScreen(Contact data) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, DisplayContactFragment.newInstance(data), "viewContact")
                .addToBackStack("null")
                .commit();
    }

    @Override
    public void toListScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new ListContactFragment(), "ListContact")
                .commit();
    }

    @Override
    public void toUpdateScreen(Contact s) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, UpdateContactFragment.newInstance(s), "UpdateContact")
                .addToBackStack("null")
                .commit();
    }

    @Override
    public void navListScreen() {
        flag = true;
//        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new ListContactFragment(), "ListContact")
                .commit();
    }

    @Override
    public void backToList() {
//        getSupportFragmentManager().popBackStack();
        flag = true;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new ListContactFragment(), "ListContact")
                .commit();
    }

    @Override
    public void detailPage(Contact s) {
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, DisplayContactFragment.newInstance(s), "viewContact")
                .addToBackStack("null")
                .commit();
    }

    @Override
    public void onBackPressed() {
        ListContactFragment fragmentView = (ListContactFragment) getSupportFragmentManager().findFragmentByTag("ListContact");
        if (fragmentView != null) {
            getSupportFragmentManager().beginTransaction().remove(fragmentView).commit();
        }
        if(flag){
            getSupportFragmentManager().popBackStack();
            flag = false;
        }
//        Log.d("demo", "onBackPressed: " + getSupportFragmentManager().getBackStackEntryCount());

        super.onBackPressed();
    }

    public void reloadContacts(String response) {
        MainActivity.contacts.clear();
        String[] joined_str = response.split("\n");
        if (joined_str.length > 0) {
            for (String str : joined_str) {
                if (!str.isEmpty()) {
                    MainActivity.contacts.add(new Contact(str));
                }
            }
        }
    }
}