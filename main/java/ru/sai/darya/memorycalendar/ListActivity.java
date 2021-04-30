package ru.sai.darya.memorycalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

import ru.sai.darya.memorycalendar.model.Memory;

import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_DATE;
import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_DESCRIPTION;
import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_LAT;
import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_LON;
import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_NAME;
import static ru.sai.darya.memorycalendar.DBHelper.COLUMN_PHOTO_PATH;
import static ru.sai.darya.memorycalendar.DBHelper.TABLE_NAME;

public class ListActivity extends AppCompatActivity {

    private ArrayList<Memory> listMemorise = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        readFromDb(listMemorise);
        RecyclerView list = findViewById(R.id.lvListMemorise);
        list.setAdapter(new MemoryAdapter());
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    public void recyclerViewListClicked(View v, int position){
        finish();
    }

    private void readFromDb(ArrayList<Memory> listMemorise) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_NAME, COLUMN_DATE, COLUMN_DESCRIPTION, COLUMN_PHOTO_PATH, COLUMN_LAT, COLUMN_LON},
                null,
                null,
                null,
                null,
                COLUMN_DATE+" ASC");

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
            int description = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int datePhoto = cursor.getColumnIndex(COLUMN_PHOTO_PATH);
            int lat = cursor.getColumnIndex(COLUMN_LAT);
            int lon = cursor.getColumnIndex(COLUMN_LON);
            do {
                LatLng lng = new LatLng(cursor.getDouble(lat), cursor.getDouble(lon));
                Memory memory = new Memory(
                        cursor.getString(dateIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(description),
                        cursor.getString(datePhoto),
                        lng);
                listMemorise.add(memory);
            } while (cursor.moveToNext());

            cursor.close();
            System.out.println("");
            dbHelper.close();
        }
    }

    static class MemoryHolder extends RecyclerView.ViewHolder{

        private final TextView tvName;
        private final TextView tvDate;
        private final TextView tvDescription;
        private final ImageView ivPhoto;
        private final View root;

        public MemoryHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewName);
            tvDate = itemView.findViewById(R.id.textViewDate);
            tvDescription = itemView.findViewById(R.id.textViewDescription);
            ivPhoto = itemView.findViewById(R.id.imageViewPhoto);
            root = itemView;
        }
    }


    class MemoryAdapter extends RecyclerView.Adapter<MemoryHolder> {

        public MemoryAdapter(){
            super();
        }

        @NonNull
        @Override
        public MemoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_memory, parent, false);
            return new MemoryHolder(row);
        }

        @Override
        public void onBindViewHolder(@NonNull MemoryHolder holder, int position) {
            Memory memory = listMemorise.get(position);
            holder.tvName.setText(memory.getNameEvent());
            holder.tvDate.setText(memory.getData());
            holder.tvDescription.setText(memory.getComment());
            holder.root.setOnClickListener(view -> {
                recyclerViewListClicked(view, position);
            });
        }

        @Override
        public int getItemCount() {
            return listMemorise.size();
        }
    }
}