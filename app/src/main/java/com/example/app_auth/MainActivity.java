package com.example.app_auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    // Definimos los campos
    TextView editNombre;
    TextView editGenero;
    TextView editCorreo;

    TextView textResponse;

    ProgressDialog progreso;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // tomamos el control
        editNombre = (EditText)findViewById(R.id.editNombre);
        editGenero = (EditText)findViewById(R.id.editGenero);
        editCorreo = (EditText)findViewById(R.id.editCorreo);

        textResponse = (TextView)findViewById(R.id.textResponse);
        Button btnSend = (Button) findViewById(R.id.btnSend);

        request = Volley.newRequestQueue(this);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarAPI();
            }
        });
    }

    private void cargarAPI() {


        String url = "https://gorest.co.in/public/v1/users";
        JSONObject usuario = new JSONObject();
        String Nombre = editNombre.getText().toString();
        String genero = editGenero.getText().toString();
        String correo = editCorreo.getText().toString();
        if (Nombre.length() < 1) {
            Toast.makeText(this, "Debes ingresar el nombre", Toast.LENGTH_LONG).show();
            return;
        }else if (genero.length() < 1) {
            Toast.makeText(this, "Debes ingresar un genero (male/female)", Toast.LENGTH_LONG).show();
            return;
        } else if (correo.length() < 1) {
            Toast.makeText(this, "Debes ingresar un correo", Toast.LENGTH_LONG).show();
            return;
        }

        progreso = new ProgressDialog(this);
        progreso.setTitle("Comunicando con la API...");
        progreso.show();

        try {
            usuario.put("name", Nombre);
            usuario.put("gender", genero);
            usuario.put("email", correo);
            usuario.put("status", "active");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, usuario, this,this ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + "ff23ee8fb342c92ef23fa314693db3ca199cdc4cb139417990ad370898562f76");
                //params.put("Accept-Language", "fr");

                return params;
            }
        };
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(this, "Ha existido un error", Toast.LENGTH_LONG).show();
        textResponse.setText(error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();
        Toast.makeText(this, "Se a registrado correctamente", Toast.LENGTH_LONG).show();
        JSONObject data = null;
        try {
            data = response.getJSONObject("data");
            textResponse.setText("El usuario se a creado correctamente: \nid: " + data.getString("id") +
                    "\nNombre: "+ data.getString("name")+
                    "\nCorreo: " + data.getString("email")
            );
        } catch (JSONException e) {
            Toast.makeText(this, "Error al presentar los datos", Toast.LENGTH_LONG).show();

        }

        editNombre.setText("");
        editGenero.setText("");
        editCorreo.setText("");

    }



}