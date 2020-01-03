/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.List;

/**
 *
 * @author Rasmus2
 */
public class ResponceDto {
    private List<String> jokes;
    private String reference;

    public ResponceDto(List<String> jokes, String reference) {
        this.jokes = jokes;
        this.reference = reference;
    }

    public List<String> getJokes() {
        return jokes;
    }

    public void setJokes(List<String> jokes) {
        this.jokes = jokes;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
    
    
    
}
