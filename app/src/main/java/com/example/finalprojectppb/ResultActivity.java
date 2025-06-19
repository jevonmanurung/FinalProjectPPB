package com.example.finalprojectppb;

import com.example.finalprojectppb.database.AppDatabase;
import com.example.finalprojectppb.database.ScanHistory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ResultActivity extends AppCompatActivity {

    private TextView resultText;
    private Button backButton;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Ambil data dari Intent
        String predictedClass = getIntent().getStringExtra("predicted_class");
        double confidence = getIntent().getDoubleExtra("confidence", 0.0);

        // Cek apakah predictedClass null atau kosong
        String result;
        if (predictedClass == null || predictedClass.trim().isEmpty()) {
            result = "Deteksi: Tidak ada penyakit kulit";
        } else {
            result = "Deteksi: " + predictedClass;
            // Atau jika ingin sertakan confidence:
            // result = "Deteksi: " + predictedClass + "\n" +
            //          "Tingkat Keyakinan: " + String.format("%.2f", confidence * 100) + "%";
        }

        // Tampilkan hasil ke TextView
        resultText = findViewById(R.id.resultText);
        resultText.setText(result);

        // Tombol kembali
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "riwayat-db").allowMainThreadQueries().build(); // gunakan async di produksi

        ScanHistory history = new ScanHistory();
        history.predictedClass = predictedClass;
        history.confidence = confidence;
        history.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        db.scanHistoryDao().insert(history);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResultActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
