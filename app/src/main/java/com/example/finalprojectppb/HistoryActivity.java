package com.example.finalprojectppb;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.finalprojectppb.database.AppDatabase;
import com.example.finalprojectppb.database.ScanHistory;
import com.example.finalprojectppb.database.ScanHistoryDao;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> historyDisplayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = findViewById(R.id.historyListView);
        historyDisplayList = new ArrayList<>();

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "riwayat-db").allowMainThreadQueries().build();

        List<ScanHistory> historyList = db.scanHistoryDao().getAllHistory();


        for (ScanHistory item : historyList) {
            String text = "Deteksi: " + item.predictedClass + "\n"
                    + "Keyakinan: " + String.format("%.2f", item.confidence * 100) + "%\n"
                    + "Waktu: " + item.timestamp;
            historyDisplayList.add(text);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyDisplayList);
        listView.setAdapter(adapter);
    }
}
