package com.example.administrator.speedviocatcher;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalRecordFragment extends Fragment {
    ListView lists;
    SQLiteDatabase dbase;
    public static String lisensya2= "";
    //public static String lisensya2= "";
    //String nameList[]={"XYZ 123","BNY 321","13C 123","LEX 123","GO 213"};

    public LocalRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_listview, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Offline Violation");

        DBHelperTwo helper=new DBHelperTwo(getActivity(),"VioFile",1);
        dbase = helper.getWritableDatabase();

        //dbase retrieve
        Cursor rsCursor;

        String  [] rsFields={"vioid","datetime","license","speed","vehicle"};
        rsCursor=dbase.query("VioFile",rsFields,null,null,null,null,null,null);
        rsCursor.moveToFirst();

        Integer c=rsCursor.getCount();
        String arr[]=new String[c];

        if(!rsCursor.isAfterLast())
        {
            Integer number=0;
            while(number<c){
                String localdatetime=rsCursor.getString(0);
                //Integer iii=rsCursor.getInt(0);
                //String o=iii.toString();
                arr[number]=localdatetime;
                rsCursor.moveToNext();
                number++;
            }
        }
        //display
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(), R.layout.listview_text,R.id.simpleTextView, arr);

        lists=(ListView)getActivity().findViewById(R.id.simpleListView);
        lists.setAdapter(arrayAdapter);

        rsCursor.close();
        //DIALOG
        lists=getListView();
    }

    public ListView getListView() {
        // TODO Auto-generated method stub
        lists.setTextFilterEnabled(true);
        lists.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view,int position,long id) {
                DBHelperTwo helper = new DBHelperTwo(getActivity(), "VioFile", 1);
                dbase = helper.getWritableDatabase();
                SharedPreferences lisensyas = getActivity().getSharedPreferences("MyApp", MODE_PRIVATE);
                lisensya2=lisensyas.getString("lisensya",  "UNKNOWN");

                Cursor rsCursor;

                String[] rsFields = {"vioid", "datetime", "license", "speed", "vehicle"};
                rsCursor = dbase.query("VioFile", rsFields, null, null, null, null, null, null);
                rsCursor.moveToFirst();

                Integer c = rsCursor.getCount();

                if (!rsCursor.isAfterLast()) {
                    Integer number = 0;
                    while (number<c) {
                        String dblicense = rsCursor.getString(2);
                        String lisc = lisensya2;
                        if (dblicense.equals(lisc)) {
                            Toast.makeText(getActivity(),rsCursor.getString(1),Toast.LENGTH_LONG).show();
                        }
                        rsCursor.moveToNext();
                        number++;
                    }
                }
                rsCursor.close();
            }});
        return null;
    }
}
