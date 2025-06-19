package com.example.finalprojectppb.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "scan_history")
public class ScanHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String predictedClass;
    public double confidence;
    public String timestamp;
}
