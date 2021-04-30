package ru.sai.darya.memorycalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.sai.darya.memorycalendar.model.Memory;

import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_NAME;
import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_DATE;
import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_DESCRIPTION;
import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_PHOTO_PATH;
import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_LAT;
import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_LON;
import static ru.sai.darya.memorycalendar.DBHelper.TABLE_NAME;

public class MemoryActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA = 843;
    final String TAG = "myLogs";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.buttonSave)
    Button btnSave;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.buttonBack)
    Button btnBack;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.buttonPhoto)
    Button btnPhoto;

    Memory memory;
    File photoFile;
    ImageView imageView;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        createDirectory();
        imageView = (ImageView) findViewById(R.id.imageView);

        MemoryFields holder = new MemoryFields(findViewById(R.layout.activity_memory));

        ButterKnife.bind(this);
        btnSave.setOnClickListener(s-> {
            String name = holder.etNameEvent.getText().toString();
            System.out.println("Название: "+ name);
            
            String date = holder.etDate.getText().toString();
            System.out.println("Дата: "+ date);
            
            String comm = holder.etDescription.getText().toString();
            System.out.println("Описание: "+ comm);
            
            LatLng coord = holder.coord;
            System.out.println("Координаты "+ coord);

            // String photoPath = photoFile.getPath();
            String photoPath = "";

            memory = new Memory(date, name, comm, photoPath, coord);
            insertToDb(memory);
            finish();
        });

        btnBack.setOnClickListener(s->{
            finish();
        });

        btnPhoto.setOnClickListener(s-> {
            Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri());
            startActivityForResult(takeIntent, REQUEST_CODE_CAMERA);
        });
    }

    private void insertToDb(Memory memory){
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, memory.getNameEvent());
        values.put(COLUMN_DATE, memory.getData());
        values.put(COLUMN_DESCRIPTION, memory.getComment());
        values.put(COLUMN_LAT, memory.getCoord().latitude);
        values.put(COLUMN_LON, memory.getCoord().longitude);

        long newRowId = db.insert(TABLE_NAME, null, values);
        System.out.println("Запись успешно добавлена в БД");
        dbHelper.close();
    }

    private Uri generateFileUri() {
        File file = null;
        file = new File(photoFile.getPath() + "/" + "photo_"
                + System.currentTimeMillis() + ".jpg");
        Log.d(TAG, "fileName = " + file);
        return FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
    }

    private void createDirectory() {
        photoFile = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyFolder");
        if (!photoFile.exists())
            photoFile.mkdirs();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    Log.d(TAG, "Intent is null");
                } else {
                    Log.d(TAG, "Photo uri: " + data.getData());
                    Bundle bndl = data.getExtras();
                    if (bndl != null) {
                        Object obj = data.getExtras().get("data");
                        if (obj instanceof Bitmap) {
                            Bitmap bitmap = (Bitmap) obj;
                            Log.d(TAG, "bitmap " + bitmap.getWidth() + " x "
                                    + bitmap.getHeight());
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "Canceled");
            }
        }
    }

    class MemoryFields{

        private final EditText etNameEvent;
        private final EditText etDate;
        private final EditText etDescription;
        private final ImageView ivForecast;
        private final LatLng coord;
        private final View root;

        public MemoryFields(@NonNull View itemView) {
            etNameEvent = findViewById(R.id.editTextName);
            etDate = findViewById(R.id.editTextDate);
            etDescription = findViewById(R.id.multiAutoCompleteTextView);
            ivForecast = findViewById(R.id.imageView);
            coord = LocationActivity.getLocation();
            root = itemView;
        }
    }
}
