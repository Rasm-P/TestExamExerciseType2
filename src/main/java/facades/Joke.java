/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

/**
 *
 * @author Rasmus2
 */
public class Joke {

    private String category;
    private String joke;

    public Joke(String category, String joke) {
        this.category = category;
        this.joke = joke;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

}
