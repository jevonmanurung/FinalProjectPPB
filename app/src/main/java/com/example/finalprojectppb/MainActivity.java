package com.example.finalprojectppb;

import com.example.finalprojectppb.utils.FileUtils;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Button;

import org.json.JSONObject;
import org.json.JSONArray;




import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_GALLERY_IMAGE = 1;
    private Uri photoUri;
    private File photoFile;
    private TextView resultTextView;
    private static final int REQUEST_PICK_IMAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);

        Button cameraButton = findViewById(R.id.cameraButton);
        Button galleryButton = findViewById(R.id.galleryButton);

        cameraButton.setOnClickListener(v -> dispatchTakePictureIntent());
        galleryButton.setOnClickListener(v -> openGallery());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Izin kamera diperlukan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Gagal membuat file gambar", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this,
                        "com.example.finalprojectppb.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && photoFile != null) {
                sendImageToRoboflow(photoFile);
            } else if (requestCode == REQUEST_GALLERY_IMAGE && data != null) {
                Uri imageUri = data.getData();
                File imageFile = new File(FileUtils.getPath(this, imageUri));
                sendImageToRoboflow(imageFile);
            }
        }
    }

    private File createTempFileFromUri(Uri uri) throws IOException {
        InputStreamReader inputStream = new InputStreamReader(getContentResolver().openInputStream(uri));
        File tempFile = File.createTempFile("selected_image", ".jpg", getCacheDir());
        FileOutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[4096];
        int bytesRead;
        InputStream in = getContentResolver().openInputStream(uri);
        while ((bytesRead = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        in.close();
        outputStream.close();
        return tempFile;
    }


    private void sendImageToRoboflow(File imageFile) {
        new Thread(() -> {
            try {
                String boundary = "Boundary-" + System.currentTimeMillis();
                URL url = new URL("https://detect.roboflow.com/skin-disease-7u8pj-xcpco/1?api_key=WhGpxXzByn18X8tooGvr");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                outputStream.writeBytes("--" + boundary + "\r\n");
                outputStream.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + imageFile.getName() + "\"\r\n");
                outputStream.writeBytes("Content-Type: image/jpeg\r\n\r\n");

                FileInputStream fileInputStream = new FileInputStream(imageFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.writeBytes("\r\n");

                fileInputStream.close();
                outputStream.writeBytes("--" + boundary + "--\r\n");
                outputStream.flush();
                outputStream.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Log.d("RoboflowResponseFull", response.toString());

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray predictionsArray = jsonResponse.getJSONArray("predictions");

                Log.d("RoboflowPredictionsLength", String.valueOf(predictionsArray.length()));

                String topClass = "";
                double topConfidence = -1.0;

                for (int i = 0; i < predictionsArray.length(); i++) {
                    JSONObject prediction = predictionsArray.getJSONObject(i);
                    String className = prediction.getString("class");
                    double confidence = prediction.getDouble("confidence");

                    Log.d("RoboflowPrediction", "Class: " + className + ", Confidence: " + confidence);

                    if (confidence > topConfidence) {
                        topConfidence = confidence;
                        topClass = className;
                    }
                }

                String finalTopClass = topClass;
                double finalTopConfidence = topConfidence;

                runOnUiThread(() -> {
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("predicted_class", finalTopClass);
                    intent.putExtra("confidence", finalTopConfidence);
                    startActivity(intent);
                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("RoboflowError", e.toString());
            }
        }).start();
    }


}