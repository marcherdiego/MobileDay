package uy.com.nerdscorner.mobileday;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Object client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                /** Invocación a servicio **/
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                /** Actualizar la UI **/
            }

        }.execute();
    }
}
