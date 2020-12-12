package com.studio.parseviewpaises;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickListener {

    private RecyclerView recyclerView;
    private AdpterListaPaises adapter;
    private List<Paises> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!list.isEmpty())
            return;

        String url ="https://restcountries.eu/rest/v2/lang/pt";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object = response.getJSONObject(i);

                                Paises paises = new Paises();

                                String coordenadas = object.getString("latlng");


                                paises.setBandeira(object.getString("flag"));
                                paises.setNome(object.getString("name"));
                                paises.setLatitude(coordenadas.substring(1,coordenadas.indexOf(",")));
                                paises.setLongetude( coordenadas.substring(coordenadas.indexOf(",") + 1, coordenadas.length() - 1));

                                list.add(paises);
                                onResume();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,"Erro " + error.getMessage(),Toast.LENGTH_LONG);

                    }
                });

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerPaises);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        adapter = new AdpterListaPaises(list,getLayoutInflater());
        adapter.setRecyclerOnClickListener(MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    public void onClickListener(View view, int position) {

        Intent intent = new Intent(MainActivity.this,MapsActivity.class);
        intent.putExtra("Lat",list.get(position).getLatitude());
        intent.putExtra("Lon",list.get(position).getLongetude());
        intent.putExtra("Nome",list.get(position).getNome());
        startActivity(intent);

    }

}