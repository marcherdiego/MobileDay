package uy.com.nerdscorner.mobileday;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = (TextView) findViewById(R.id.progress);
    }

    public void tomarFoto(View view) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    public void invocarServicio(View view) {
        new AsyncTask<Void, Integer, String>() {
            private static final int ITERACIONES_TOTAL = 100;

            @Override
            protected String doInBackground(Void... params) {
                try {
                    for (int i = 0; i < ITERACIONES_TOTAL; ++i) {
                        publishProgress(i * 100 / ITERACIONES_TOTAL);
                        Thread.sleep(50);
                    }
                    return "éxito";
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "error";
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progress.setText("Completado " + values[0] + "%");
            }

            @Override
            protected void onPostExecute(String resultado) {
                progress.setText(String.format("Invocación completada con %s", resultado));
            }
        }.execute();
    }
}
