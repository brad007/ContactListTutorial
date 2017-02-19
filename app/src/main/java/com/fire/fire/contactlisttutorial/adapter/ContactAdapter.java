package com.fire.fire.contactlisttutorial.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fire.fire.contactlisttutorial.R;

/**
 * Created by brad on 2017/02/18.
 */


//CursorRecyclerViewAdapter class that I added to this project, is just boilerplate code that
//allows you to use LoaderManger, ContentProviders and Cursors with recyclerviews

public class ContactAdapter extends CursorRecyclerViewAdapter<ContactAdapter.ContactViewHolder> {
    public ContactAdapter(Context context, Cursor cursor, String id) {
        super(context, cursor, id);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_contact,
                parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder viewHolder, Cursor cursor) {
        //Now we can handle onBindViewHolder

        long contactId = getItemId(cursor.getPosition());

        //Setting the username
        String username = cursor.getString(cursor.getColumnIndex(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
                        ContactsContract.Contacts.DISPLAY_NAME
        ));

        viewHolder.contactDisplayNameTextView.setText(username);

        //Setting the photo

        long photoId = cursor.getLong(cursor.getColumnIndex(
                ContactsContract.Data.PHOTO_ID
        ));

        if (photoId != 0) {
            Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                    contactId);
            Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo
                    .CONTENT_DIRECTORY);

            viewHolder.contactDisplayImageView.setImageURI(photoUri);
        } else {
            viewHolder.contactDisplayImageView.setImageResource(R.drawable.facebook_avatar);
        }
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        ImageView contactDisplayImageView;
        TextView contactDisplayNameTextView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            contactDisplayImageView = (ImageView) itemView.findViewById(R.id.contact_display);
            contactDisplayNameTextView = (TextView) itemView.findViewById(R.id.contact_display_name);
        }
    }
}
