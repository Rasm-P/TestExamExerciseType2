/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import dto.ResponceDto;
import net.minidev.json.JSONObject;

/**
 *
 * @author Rasmus2
 */
public class JokeFacade {

    private static JokeFacade instance;
    private static String siteUrl = "https://api.chucknorris.io/jokes/random?category=";
    Gson g = new Gson();
    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);

    //Private Constructor to ensure Singleton
//    private JokeFacade() {
//    }
    public static JokeFacade getFacade() {
        if (instance == null) {
            instance = new JokeFacade();
        }
        return instance;
    }

    public ResponceDto jokes(String[] categoryArray) throws InterruptedException, ExecutionException {
        final List<String> jokes = new ArrayList();
        ExecutorService executor = Executors.newFixedThreadPool(categoryArray.length);
        List<Future<String>> list = new ArrayList();

        for (int i = 0; i < categoryArray.length; i++) {
            final String category = categoryArray[i];

            Future<String> future = executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    String s = returnJokeGivenCategory(category);
                    jokes.add(s);
                    return s;
                }
            });
            list.add(future);
        }
        for (Future fut : list) {
            System.out.println(fut.get());
        }
        executor.shutdown();

        ResponceDto responceDto = new ResponceDto(jokes, siteUrl);
        return responceDto;
    }

    public String returnJokeGivenCategory(String Category) throws MalformedURLException, ProtocolException, IOException {
        URL url = new URL(siteUrl + Category);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setRequestProperty("User-Agent", "server");
        String jsonStr;
        try (Scanner scan = new Scanner(con.getInputStream())) {
            jsonStr = null;
            if (scan.hasNext()) {
                jsonStr = scan.nextLine();
            }
        }

        JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject();
        JSONObject obj = new JSONObject();
        obj.put("category", Category);
        obj.put("joke", jsonObject.get("value").getAsString());
        return g.toJson(obj);
    }

//    public static void main(String[] args) throws ProtocolException, IOException, InterruptedException, ExecutionException {
//        System.out.println(jokes("food"));
//    }
}
