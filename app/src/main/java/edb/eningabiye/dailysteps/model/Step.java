package edb.eningabiye.dailysteps.model;

public class Step {

    private String date;
    private String month;



    private String year;
    private int steps;
    private int previous_steps;
    private User user;

    public Step(String date, int steps, int previous_steps, User user) {
        this.date = date;
        this.steps = steps;
        this.user = user;
        this.previous_steps = previous_steps;
    }
    public Step(String date, int steps, int previous_steps, String month, String year) {
        this.date = date;
        this.steps = steps;
        this.user = user;
        this.previous_steps = previous_steps;
        this.month =month;
        this.year=year;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }
    public int getPercent(){
        if(previous_steps == 0){
            return 0;
        }
        int diff = steps - previous_steps;
        return (100*diff)/previous_steps;
    }
    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
}
