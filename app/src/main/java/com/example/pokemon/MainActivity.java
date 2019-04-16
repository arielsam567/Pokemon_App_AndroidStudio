package com.example.pokemon;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    private ImageView imagemV;
    private String resposta;
    public Button b1;
    public Button b2;
    public Button b3;
    public Button b4;
    // https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button prox;

        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        prox = findViewById(R.id.button6);
        imagemV = findViewById(R.id.imageView);
        InputStream in;
        String urlJSON = "https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json";
        in = leJson(urlJSON);
        resposta= convertStreamToString(in);// covert the input stream to a string
        final List pokemon = lePokemonsDeJSONString();





        Pokemon p = (Pokemon) pokemon.get(1);
        String  urlPokemon = p.getUrl();
        downloadImagem(urlPokemon);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Random gerador = new Random();
                Pokemon a = (Pokemon) pokemon.get(gerador.nextInt(150));
                Pokemon b = (Pokemon) pokemon.get(gerador.nextInt(150));
                Pokemon c = (Pokemon) pokemon.get(gerador.nextInt(150));
                Pokemon d = (Pokemon) pokemon.get(gerador.nextInt(150));
                b1.setText(a.getNome());
                b2.setText(b.getNome());
                b3.setText(c.getNome());
                b4.setText(d.getNome());
                downloadImagem(c.getUrl());




            }
        };
        prox.setOnClickListener(onClickListener);

        View.OnClickListener c1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Parabens!!  Você acertou", Toast.LENGTH_SHORT).show();
            }
        };
        View.OnClickListener c2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Putz, talvez na proxima", Toast.LENGTH_SHORT).show();
            }
        };
        b3.setOnClickListener(c1);
        b1.setOnClickListener(c2);
        b2.setOnClickListener(c2);
        b4.setOnClickListener(c2);









    }

    public InputStream leJson(String urljson){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpURLConnection urlConnection;
        InputStream in = null;
        try {
            // the url we wish to connect to
            URL url = new URL(urljson);// open the connection to the specified URL
            urlConnection = (HttpURLConnection) url.openConnection();// get the response from the server in an input stream
            in = new BufferedInputStream(urlConnection.getInputStream());
            return in;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void downloadImagem(String urlJson){

        StringBuilder stringBuilder = new StringBuilder(urlJson);
        stringBuilder.insert(4, 's');
        String imgUrl = stringBuilder.toString() ;
        //imgUrl = "http://www.serebii.net/pokemongo/pokemon/002.png";
        ImagemDownload imagemDownloader = new ImagemDownload();
        try{
            Bitmap imagem = imagemDownloader.execute(imgUrl).get();
            imagemV.setImageBitmap(imagem);
        }catch (Exception e){
            Log.e(TAG, "downloadImagem: Impossivel baixar imagem" + e.getMessage() );
        }
    }

    private class ImagemDownload extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings){
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }catch (Exception e){
                Log.e(TAG, "doInBackground:  erro ao baixar imagem" + e.getMessage() );
            }
            return null;
        }
    }



    public String convertStreamToString(InputStream is) { // converte o que lepara ums string
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }




    private List<Pokemon> lePokemonsDeJSONString() { // poe os pokemons na lista
        List<Pokemon> listaPokemons = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(resposta);
            JSONArray pokemons = json.getJSONArray("pokemon");
            for (int i = 0; i < pokemons.length(); i++) {
                JSONObject pokemon = pokemons.getJSONObject(i);
                Pokemon f = new Pokemon(
                        pokemon.getString("name"),
                        pokemon.getString("img"),
                        pokemon.getInt("id")
                );
                listaPokemons.add(f);
            }
        } catch (JSONException e) {
            System.err.println("Erro fazendo parse de String JSON: " + e.getMessage());
        }
        return listaPokemons;
    }
}
