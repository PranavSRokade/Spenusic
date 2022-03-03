package com.example.spenusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                ArrayList<Music> mySongs = getMusic(MainActivity.this);
                ArrayList<String> songs = getSongs(mySongs);

                adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, songs);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String updated = listView.getItemAtPosition(i).toString();

                        for(int x = 0; x < mySongs.size(); x++){
                            if(mySongs.get(x).name.equals(updated)){
                                i = x;
                            }
                        }

                        Intent intent = new Intent(MainActivity.this, PlaySong.class);
                        intent.putExtra("songList", mySongs);
                        intent.putExtra("position", i);
                        intent.putExtra("name", mySongs.get(i).name);

                        startActivity(intent);
                    }
                });

                listView.setAdapter(adapter);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<String> getSongs(ArrayList<Music> arrayList){
        ArrayList<String> songs = new ArrayList<>();

        for(int i = 0; i < arrayList.size(); i++){
            songs.add(arrayList.get(i).name);
        }

        return songs;
    }

    public ArrayList<Music> getMusic(Context context) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        ArrayList<Music> arrayList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int data = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            while (cursor.moveToNext()) {
                String name = cursor.getString(title);
                String path = cursor.getString(data);
                if (!name.contains("Whatsapp") && !name.contains("-WA")) {
                    Music music = new Music();
                    music.setName(name);
                    music.setPath(path);

                    arrayList.add(music);
                }
            }
        }
        cursor.close();
        return arrayList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }
}