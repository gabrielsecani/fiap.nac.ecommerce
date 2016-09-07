package br.com.gltr.ecommerce;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EcommerceActivity extends AppCompatActivity {

    private EditText edtCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecommerce);
    }

    public void buscar(View v) {

        edtCodigo = (EditText) findViewById(R.id.edtCodigo);
        BuscarItemTask task = new BuscarItemTask();
        task.execute(edtCodigo.getText().toString());

    }

    public void inserir(View v) {

        Intent i = new Intent(this, CadastroItemActivity.class);
        startActivity(i);

    }

    private class BuscarItemTask extends AsyncTask<String, Void, String> {

        private ProgressDialog progress;

        protected void onPreExecute() {
            progress = ProgressDialog.show(EcommerceActivity.this, "Aguarde...", "Conectando com o servidor.");
        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://10.20.22.41:8080/MercadoFiap2/ecommerce?id=1&codigo=" + strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() == 200) {

                    BufferedReader stream = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));

                    String linha = "";
                    StringBuilder builder = new StringBuilder();

                    while ((linha = stream.readLine()) != null) {
                        builder.append(linha);
                    }

                    connection.disconnect();
                    return builder.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String s) {

            progress.dismiss();
            if (s != null) {
                try {
                    JSONObject json = new JSONObject(s);

                    int codigo = json.getInt("codigo");
                    String descricao = json.getString("descricao");
                    int quantidade = json.getInt("quantidade");
                    double preco = json.getDouble("preco");

                    Toast.makeText(EcommerceActivity.this,
                            codigo + ": " + descricao + " - " + quantidade + " - " + preco,
                            Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
