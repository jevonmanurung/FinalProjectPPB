package com.example.finalprojectppb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); // ini adalah layout yang menampilkan semua card

        // Akses CardView "Scan Kulit"
        CardView cardScanKulit = findViewById(R.id.cardScanKulit);

        // Klik pada "Scan Kulit" → buka MainActivity
        cardScanKulit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Akses CardView "Your Scans"
        CardView cardRiwayat = findViewById(R.id.cardRiwayatScan);

        // Klik pada "Your Scans" → buka HistoryActivity
        cardRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        // TODO: Tambahkan aksi klik lain jika perlu untuk card-card lainnya
    }
}
