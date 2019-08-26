package com.example.administrator.speedviocatcher;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Administrator on 9/22/2017.
 */

public class FirebaseListenerRef {

    private ValueEventListener listener;
    private DatabaseReference ref;

    public FirebaseListenerRef(DatabaseReference ref, ValueEventListener listener) {
        this.listener = listener;
        this.ref = ref;

        // Start listening on this ref.
        ref.addValueEventListener(listener);
    }

    public void detach() {
        ref.removeEventListener(listener);
    }
}