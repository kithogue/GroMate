package com.example.gromate.gromate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseUtil {

    private static  DatabaseReference databaseReference;
    private static String uid;

    private DatabaseUtil() {
        //do nothing
    }

    public static DatabaseReference getDatabase() {
        if (databaseReference == null) {
            databaseReference =FirebaseDatabase.getInstance().getReference();
        }
        return  databaseReference;
    }

    public static String getUid() {
        if (uid == null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return uid;
    }

    public static void setDatabaseReference(DatabaseReference databaseReference) {
        DatabaseUtil.databaseReference = databaseReference;
    }
    public static void setUid(String uid) {
        DatabaseUtil.uid = uid;
    }

}
