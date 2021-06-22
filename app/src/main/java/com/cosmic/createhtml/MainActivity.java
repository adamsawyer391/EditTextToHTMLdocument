package com.cosmic.createhtml;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etHTML;
    private StringBuilder stringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateUI();
    }

    private void updateUI(){
        etHTML = findViewById(R.id.etHTML);
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    private void convertToHtml(String s){
        String _lineBreak = "\n";
        stringBuilder = new StringBuilder()
                .append("<!doctype html>")
                .append(_lineBreak)
                .append(_lineBreak)
                .append("<html lang=\"en\" >")
                .append(_lineBreak)
                .append("<head>")
                .append(_lineBreak)
                .append("<meta charset=\"utf-8\" >")
                .append(_lineBreak)
                .append("<title>Android to HTML</title>")
                .append(_lineBreak)
                .append("</head>")
                .append("<body>")
                .append(_lineBreak)
                .append("<h1>We Converted Android EditTexts to HTML!</h1>")
                .append(_lineBreak)
                .append("<p>")
                .append(s)
                .append("</p>")
                .append("</body>")
                .append("</html>");
        createHtmlFile(stringBuilder);
    }

    private void createHtmlFile(StringBuilder stringBuilder){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            String filename = System.currentTimeMillis() + ".html";
            try{
                FileOutputStream fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                fileOutputStream.write(stringBuilder.toString().getBytes());
                fileOutputStream.close();
                File file = new File(getFilesDir(), filename);
                Uri uri = FileProvider.getUriForFile(this, "com.cosmic.createhtml.fileprovider", file);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_SUBJECT, "HTML Document");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Crreate HTML"));
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            createHtmlFile(stringBuilder);
        }
    }

    @Override
    public void onClick(View v) {
        String text = etHTML.getText().toString().trim();
        convertToHtml(text);
    }
}