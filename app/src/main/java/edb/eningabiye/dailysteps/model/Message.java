package edb.eningabiye.dailysteps.model;

public class Message {
    private String nom, date, message, steps;

    public Message(String nom, String date, String message, String steps) {
        this.nom = nom;
        this.date = date;
        this.message = message;
        this.steps = steps;
    }

    public String getNom() {
        return nom;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getSteps() {
        return steps;
    }
}
