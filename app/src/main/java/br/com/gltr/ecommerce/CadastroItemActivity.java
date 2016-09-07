package br.com.gltr.ecommerce;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONStringer;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CadastroItemActivity extends AppCompatActivity {

    private EditText edtDescricao;
    private EditText edtQuantidade;
    private EditText edtPreco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_item);
    }

    public void inserir(View view) {

        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtQuantidade = (EditText) findViewById(R.id.edtQuantidade);
        edtPreco = (EditText) findViewById(R.id.edtPreco);

        InserirItemTask task = new InserirItemTask();
        task.execute(edtDescricao.getText().toString(), edtQuantidade.getText().toString(), edtPreco.getText().toString());

    }

    private class InserirItemTask extends AsyncTask<String, Void, Integer> {

        private ProgressDialog progress;

        protected void onPreExecute() {
            progress = ProgressDialog.show(CadastroItemActivity.this, "Aguarde...", "Conectando com o servidor.");
        }

        @Override
        protected Integer doInBackground(String... strings) {

            try {
                URL url = new URL("http://10.20.22.41:8080/MercadoFiap2/ecommerce");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Context-Type", "application/json");

                JSONStringer json = new JSONStringer();
                json.object();
                json.key("descricao").value(strings[0]);
                json.key("quantidade").value(strings[1]);
                json.key("preco").value(strings[2]);
                json.endObject();

                OutputStreamWriter stream = new OutputStreamWriter(connection.getOutputStream());
                stream.write(json.toString());
                stream.close();

                return connection.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Integer code) {

            progress.dismiss();

            if (code == 404) {
                Toast.makeText(CadastroItemActivity.this, "Cadastro realizado", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(CadastroItemActivity.this, "Erro ao cadastrar", Toast.LENGTH_LONG).show();
            }
        }
    }
}
