package uy.com.nerdscorner.mobileday;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    private static final int IMAGE_FROM_CAMERA = 1;

    private TextView progress;
    private ImageView imageView;
    private Uri currentImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        progress = (TextView) findViewById(R.id.progress);
        imageView = ((ImageView) findViewById(R.id.imageView));
    }

    public void tomarFoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                currentImageUri = createImageFile();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri);
                startActivityForResult(takePictureIntent, IMAGE_FROM_CAMERA);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Uri createImageFile() throws IOException {
        String imageFileName = "mobileDay_" + System.currentTimeMillis();
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MobileDay/");
        if (!storageDir.exists()) {
            boolean created = storageDir.mkdirs();
            if (!created) {
                throw new IOException("Directory not created");
            }
        }
        File tempFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return Uri.fromFile(tempFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            //Notificar a la galería
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, currentImageUri));
            try {
                //Obtener imagen full size
                Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), currentImageUri);
                imageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enviarFoto(View view) {
        AsyncTask<Void, Integer, Boolean> imageSender = new AsyncTask<Void, Integer, Boolean>() {
            private static final int ITERACIONES_TOTAL = 100;

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    for (int i = 0; i < ITERACIONES_TOTAL; ++i) {
                        publishProgress(i * 100 / ITERACIONES_TOTAL);
                        Thread.sleep(50);
                    }
                    return true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progress.setText("Completado " + values[0] + "%");
            }

            @Override
            protected void onPostExecute(Boolean exito) {
                if (exito) {
                    progress.setText("Envío finalizado con éxito");
                    imageView.setImageResource(android.R.color.darker_gray);
                } else {
                    progress.setText("Envío finalizado con error");
                }
            }
        };
        imageSender.execute();
    }
}
