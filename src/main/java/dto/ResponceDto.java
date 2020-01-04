/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import facades.Joke;
import java.util.List;

/**
 *
 * @author Rasmus2
 */
public class ResponceDTO {
    private List<Joke> jokes;
    private String reference;

    public ResponceDTO(List<Joke> jokes, String reference) {
        this.jokes = jokes;
        this.reference = reference;
    }

    public List<Joke> getJokes() {
        return jokes;
    }

    public void setJokes(List<Joke> jokes) {
        this.jokes = jokes;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    
    
    
}
