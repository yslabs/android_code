package com.yugasa.demo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.DrawerLayout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.RecyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.tabs)
    TabLayout tabs;
    public
    @InjectView(R.id.viewpager)
    ViewPager viewPager;
    ActionBar actionBar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    LinearLayoutManager linearLayoutManager;
    List<String> titles = new ArrayList<>();
    TextView txt_name;
    TextView txt_phonenumber;
    private Boolean flag = false;
    String mylocationcity;
    Intent intent;
    Bitmap bitmap;
    String id, name;
    String base64;
    Status status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
//       Setting actionbar
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.app_name);
        actionBar.setHomeButtonEnabled(true);
//        setting  navigation drawer

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

//          setting tabs

//        viewPager.setAdapter(new HomepagerAdapter(this, getSupportFragmentManager(), titles));
//        tabs.setupWithViewPager(viewPager);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onResume() {

        super.onResume();
        //setting click on navigation drawer item


        recyclerView.setAdapter(new SideViewAdapter(this, getResources(), new SideViewAdapter.ItemClicked() {
            @Override
            public void itemClicked(View view, int position) {
                drawerLayout.closeDrawers();
                switch (position) {
                    case 1:
                        OfficeInfo officeInfo = DroidPrefs.get(HomeActivity.this, "officeinfo", OfficeInfo.class);
                        if (officeInfo != null && officeInfo.name2 != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            View select = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_select, null);
                            final List<String> edit_select = new ArrayList<String>();
                            ListView listView = (ListView) select.findViewById(R.id.listview);
                            builder.setTitle("Select Location");
                            edit_select.add("Home");
                            edit_select.add("Office");
                            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                            listView.setAdapter(new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_single_choice,  android.R.id.text1, edit_select));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    name = edit_select.get(position);
                                }
                            });

                            builder.setView(select);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (name == null) {
                                        Toast.makeText(HomeActivity.this, "Please select an option ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent edit = new Intent(HomeActivity.this, EditProfile.class);
                                        edit.putExtra("location", name);
                                        startActivity(edit);
                                    }
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();

                        } else {
                            Intent edit = new Intent(HomeActivity.this, EditProfile.class);
                            name = "Home";
                            edit.putExtra("location", name);
                            startActivity(edit);
                        }

                        break;

                    case 2:

                        OfficeInfo office = DroidPrefs.get(HomeActivity.this, "officeinfo", OfficeInfo.class);
                        if (office != null && office.name2 != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            View select = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_select, null);
                            final List<String> edit_select = new ArrayList<String>();
                            ListView listView = (ListView) select.findViewById(R.id.listview);
                            builder.setTitle("Select Location");
                            edit_select.add("Home");
                            edit_select.add("Office");
                            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                            listView.setAdapter(new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_single_choice, android.R.id.text1, edit_select));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    name = edit_select.get(position);
                                }
                            });

                            builder.setView(select);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (name == null) {
                                        Toast.makeText(HomeActivity.this, "please select an option ", Toast.LENGTH_SHORT).show();
                                    } else if(name=="Home"){

                                        Intent edit = new Intent(HomeActivity.this, MapActivity.class);
                                        edit.putExtra("floor",0);
                                        edit.putExtra("category", name);
                                        startActivity(edit);
                                    }
                                    else if(name=="Office"){
                                        if (status != null && status.status != null) {
                                            if (status.status.equalsIgnoreCase("3")) {
                                                Intent register = new Intent(HomeActivity.this, MapActivity.class);
                                                register.putExtra("floor",0);
                                                register.putExtra("category", name);
                                                startActivity(register);

                                            }
                                            else if (status.status.equalsIgnoreCase("4")) {
                                           
                                            } else if (status.status.equalsIgnoreCase("1") || status.status.equalsIgnoreCase("2")) {
                                                

                                            } else {
                                                Intent register = new Intent(HomeActivity.this, MapActivity.class);
                                                startActivity(register);}

                                        }
                                        else {
                                          
                                        }
                                    }
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();

                        } else {
                            Intent edit = new Intent(HomeActivity.this, MapActivity.class);
                            name = "Home";

                            edit.putExtra("category", name);
                            startActivity(edit);
                        }

                        break;
                    case 3:


                        OfficeInfo office = DroidPrefs.get(HomeActivity.this, "officeinfo", OfficeInfo.class);
                        if (office1 != null && office1.name2 != null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            View select = LayoutInflater.from(HomeActivity.this).inflate(R.layout.layout_select, null);
                            final List<String> edit_select = new ArrayList<String>();
                            ListView listView = (ListView) select.findViewById(R.id.listview);
                            builder.setTitle("Select Location");
                            edit_select.add("Home");
                            edit_select.add("Office");
                            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                            listView.setAdapter(new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_single_choice, android.R.id.text1, edit_select));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    name = edit_select.get(position);
                                }
                            });

                            builder.setView(select);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (name == null) {
                                        Toast.makeText(HomeActivity.this, "please select an option ", Toast.LENGTH_SHORT).show();
                                    } else if(name=="Home"){

                                        Intent edit = new Intent(HomeActivity.this, MapActivity.class);
                                        edit.putExtra("floor",0);
                                        edit.putExtra("category", name);
                                        startActivity(edit);
                                    }
                                    else if(name=="Office"){
                                        if (status != null && status.status != null) {
                                            if (status.status.equalsIgnoreCase("3")) {
                                                Intent register = new Intent(HomeActivity.this, MapActivity.class);
                                                register.putExtra("floor",0);
                                                register.putExtra("category", name);
                                                startActivity(register);

                                            }
                                            else if (status.status.equalsIgnoreCase("4")) {
                                            } else if (status.status.equalsIgnoreCase("1") || status.status.equalsIgnoreCase("2")) {

                                            } else {
                                                Intent register = new Intent(HomeActivity.this, MapActivity.class);
                                                startActivity(register);}

                                        }
                                        else if (status == null && status.status == null  ){
                                            Toast.makeText(HomeActivity.this,"Your Registration Request has been sent to GFCS.Wait for approval",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();

                        } else {
                            Intent edit = new Intent(HomeActivity.this, MapActivity.class);
                            name = "Home";

                            edit.putExtra("category", name);
                            startActivity(edit);
                        }
                        break;
                    case 4:
                        Intent fire = new Intent(HomeActivity.this, FireStations.class);
                        fire.putExtra("key", mylocationcity);
                        startActivity(fire);
                        break;
                    case 5:
                        startActivity(new Intent(HomeActivity.this, TrainingRequestActivity.class));
                        break;
                    case 6:
                        OfficeInfo officeinfo = DroidPrefs.get(HomeActivity.this, "officeinfo", OfficeInfo.class);
                        if (officeinfo != null && officeinfo.society_name != null)
                           
                        else
                            startActivity(new Intent(HomeActivity.this, MainActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(HomeActivity.this, Tips.class));
                        break;



                }
            }
        }, userInfo));


    }


    @Override
    protected void onPostCreate (Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

//Implementing alert on back button click
    @Override
    public void onBackPressed () {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawers();
        } else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Exit")
                    .setMessage("Are you sure, you want to exit?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        }
    }
// taking image from camera
    public void SelectImage() {
        final CharSequence[] options = {"Take Photo", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
//    Converting bitmap to base 64
    public String getimage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 56, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }

}
