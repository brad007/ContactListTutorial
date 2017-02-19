package com.fire.fire.contactlisttutorial.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fire.fire.contactlisttutorial.BuildConfig;
import com.fire.fire.contactlisttutorial.R;
import com.fire.fire.contactlisttutorial.adapter.ContactAdapter;

/**
 * Quick word:
 * Thanks for watching this tutorial. If you liked it, give it a like, subscribe if you like to
 * see more. I post tutorials every weekend and I take request.
 * If you're a bit confused about ContentProviders and LoaderManagers I'll be puting a link in the
 * description that will explain it in more detail if you're interested
 */
public class MainActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<Cursor> {
    private static final String[] FROM_COLUMNS = {
            ContactsContract.Data.CONTACT_ID,
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                    ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Data.PHOTO_ID,
    };
    private static final int REQUEST_PERMISSION = 0;

    //First things first, let's add the permission to read contacts
    //Now that's that's done, let's do the UI.
    //First we'll do the recyclerview and then the contact row
    //Now that we're almost done with the UI. Let's create the adapter

    private ContactAdapter mContactAdapter;
    private RecyclerView mContactRecyclerView;

    /**
     * This Loader ID constant is used to identify which Cursor to handle. It's not really needed
     * in this application, but it's something I put it just to future proof my work incase things
     * change along the way
     */
    private static final int LOADER_ID = 1;
    //That's akk folks
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //One final thing to do is to ask the user permission this is for all devices with android
        //M+

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                .READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[]{
                            Manifest.permission.READ_CONTACTS
                    },
                    REQUEST_PERMISSION
            );
        } else {
            getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);
        }

        //Initialising LoaderManger
        getSupportLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);

        mContactRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_contacts);
        mContactRecyclerView.setHasFixedSize(true);
        mContactRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mContactAdapter = new ContactAdapter(MainActivity.this, null, ContactsContract.Data
                .CONTACT_ID);

        mContactRecyclerView.setAdapter(mContactAdapter);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(
                        MainActivity.this,
                        ContactsContract.Data.CONTENT_URI,
                        FROM_COLUMNS,
                        null,
                        null,
                        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                                ContactsContract.Contacts.DISPLAY_NAME) +
                                " ASC"
                );
            default:
                if (BuildConfig.DEBUG)
                    throw new IllegalArgumentException("no id handled");
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mContactAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContactAdapter.swapCursor(null);
    }
}
