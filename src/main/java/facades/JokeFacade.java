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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import dto.ResponceDTO;
import entities.Category;
import entities.Request;
import errorhandling.CategoryException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import javax.persistence.NoResultException;

/**
 *
 * @author Rasmus2
 */
public class JokeFacade {

    private static EntityManagerFactory emf;
    private static JokeFacade instance;
    private static final String siteUrl = "https://api.chucknorris.io/jokes/random?category=";
    Gson g = new Gson();

    //Private Constructor to ensure Singleton
    private JokeFacade() {
    }

    public static JokeFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new JokeFacade();
        }
        return instance;
    }

    public ResponceDTO jokes(String[] categoryArray) throws InterruptedException, ExecutionException, CategoryException {

        final List<Joke> jokes = new ArrayList();
        ExecutorService executor = Executors.newFixedThreadPool(categoryArray.length);
        Queue<Future<Joke>> list = new ArrayBlockingQueue(categoryArray.length);
        EntityFacade cateF = EntityFacade.getFacade(emf);
        try {
            for (int i = 0; i < categoryArray.length; i++) {
                final String category = categoryArray[i];
                Category travel = cateF.findLegalCategroy(category);
                Request r = new Request();
                travel.addToRequestList(r);
                cateF.addRequest(r);
                cateF.categoryUpdate(travel);

                Future<Joke> future = executor.submit(new Callable<Joke>() {
                    @Override
                    public Joke call() throws Exception {
                        String s = returnJokeGivenCategory(category);

                        JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
                        Joke joke = new Joke(category, jsonObject.get("value").getAsString());
                        return joke;
                    }
                });
                list.add(future);
            }

            while (!list.isEmpty()) {
                Future<Joke> j = list.poll();
                if (j.isDone()) {
                    jokes.add(j.get());
                } else {
                    list.add(j);
                }
            }

        } catch (NoResultException | CategoryException | InterruptedException | ExecutionException ex) {
            executor.shutdown();
            throw new CategoryException("One of the categories were not vaild!");
        }
        executor.shutdown();

        ResponceDTO responceDto = new ResponceDTO(jokes, siteUrl);
        return responceDto;
    }

    public String returnJokeGivenCategory(String category) throws MalformedURLException, ProtocolException, IOException {
        URL url = new URL(siteUrl + category);
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
        return jsonStr;
    }

}
