package com.will.givegreenville.models;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    private String title;
    private String description;

    // location = zip code ("postal code")
    private String location;
    private String category;

    private boolean active;

    @ColumnDefault(value = "false")
    private boolean completed;

    @ColumnDefault(value = "false")
    private boolean flagged;

    @Column(name = "image_path")
    private String imagePath;

    public Post() {
    }

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy = "post",
    cascade = CascadeType.ALL)
    private List<Consideration> considerations;

    @OneToOne
    @JoinColumn(name = "recipient")
    private User recipient;

    public List<Consideration> getConsiderations() {
        return considerations;
    }

    public List<String> getConsiderers() {
        List<Consideration> considerations = getConsiderations();
        List<String> considerers = new ArrayList<>();
        for (Consideration consideration : considerations) {
            String considererUsername = consideration.getUser().getUsername();
            considerers.add(considererUsername);
        }
        return considerers;
    }

    public void setConsiderations(List<Consideration> considerations) {
        this.considerations = considerations;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
