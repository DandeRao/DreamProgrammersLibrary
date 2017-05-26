/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mallikharjuna Rao Dande
 */
public class NewItem {
    String type;
    String title;
    String author;
    String cost;
    String userId;
    String status;
    String id;

    public NewItem() {
    }
 
    public NewItem(String type, String title, String author, String cost, String userId, String status, String id) {
        this.type = type;
        this.title = title;
        this.author = author;
        this.cost = cost;
        this.userId = userId;
        this.status = status;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public NewItem(String type, String title, String author, String cost, String userId, String status) {
        this.type = type;
        this.title = title;
        this.author = author;
        this.cost = cost;
        this.userId = userId;
        this.status = status;
    }
    
    public NewItem(String type, String title, String author, String cost, String userId) {
        this.type = type;
        this.title = title;
        this.author = author;
        this.cost = cost;
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
