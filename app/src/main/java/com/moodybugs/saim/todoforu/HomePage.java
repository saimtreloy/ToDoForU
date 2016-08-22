package com.moodybugs.saim.todoforu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class HomePage extends AppCompatActivity {

    DatabaseHelper myDatabase;
    SwipeMenuCreator swipeMenuCreator;
    SwipeMenuListView listViewSwipNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_appbar);

        Initialization();
        Floatingutton();
        populateListView();
        SwipeMenu();
        showItem();
        swipeItem();
    }

    /*  Floating Action Button*/
    public void Floatingutton(){
        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.ic_home);

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this).setContentView(icon).build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.ic_power_settings_new);

        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.ic_settings);

        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.ic_add_alert);

        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .build();

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddToDo.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Developer.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseDialogBox();
            }
        });
    }
    /*  Floating Action Button End*/
    public void Initialization(){
        myDatabase = new DatabaseHelper(this);
        listViewSwipNew = (SwipeMenuListView) findViewById(R.id.listViewSwipNew);
    }

    public void SwipeMenu(){
        swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                //deleteItem.setBackground(android.R.color.holo_red_light);
                deleteItem.setBackground(R.color.custom);
                deleteItem.setWidth(200);
                deleteItem.setIcon(R.drawable.ic_delete_forever);
                menu.addMenuItem(deleteItem);
            }
        } ;
        listViewSwipNew.setMenuCreator(swipeMenuCreator);
    }

    String[] fromFieldNames;int[] toViewID;
    public void populateListView(){
        Cursor cursor = myDatabase.getAllData();
        startManagingCursor(cursor);
        fromFieldNames = new String[] {DatabaseHelper.COL_2, DatabaseHelper.COL_4, DatabaseHelper.COL_3, DatabaseHelper.COL_1};
        toViewID = new int[] {R.id.listTitle, R.id.listDate, R.id.listDescription, R.id.listID};

        SimpleCursorAdapter myCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.item_layout,
                cursor,
                fromFieldNames,
                toViewID
        );
        listViewSwipNew.setAdapter(myCursorAdapter);
    }

    public void showItem(){
        listViewSwipNew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView ID = (TextView) view.findViewById(R.id.listID);
                final TextView TITLE = (TextView) view.findViewById(R.id.listTitle);
                TextView DESCRIPTION = (TextView) view.findViewById(R.id.listDescription);
                TextView DATE = (TextView) view.findViewById(R.id.listDate);
                String a = ID.getText().toString() + "\n" + TITLE.getText().toString() + "\n" + DESCRIPTION.getText().toString() + "\n" + DATE.getText().toString();

                Intent intent = new Intent(HomePage.this, ShowData.class);
                intent.putExtra("KeyID", ID.getText().toString());
                intent.putExtra("KeyTitle", TITLE.getText().toString());
                intent.putExtra("KeyDescription", DESCRIPTION.getText().toString());
                intent.putExtra("KeyDate", DATE.getText().toString());
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), a, Toast.LENGTH_LONG).show();
            }
        });
    }

    View v; TextView ID;
    public void swipeItem(){
        listViewSwipNew.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                v = listViewSwipNew.getChildAt(position);
                ID = (TextView) v.findViewById(R.id.listID);
                listViewSwipNew.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                        switch (index){
                            case 0:{
                                deleteItem();
                            }
                        }
                        return false;
                    }
                });
            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });
    }

    public void deleteItem(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomePage.this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this item?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                int numberOfRowDeleted = myDatabase.deleteData(ID.getText().toString());
                Toast.makeText(getApplicationContext(), numberOfRowDeleted + " rows deleted!", Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
            }
        });

        alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void CloseDialogBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure want to close this app?");
        alertDialogBuilder.setPositiveButton("Exit",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        HomePage.this.finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Rate us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/developer?id=MoodyBugs"));
                startActivity(intent);
            }
        });

        alertDialogBuilder.setNeutralButton("Like us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.facebook.com/streloy"));
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
