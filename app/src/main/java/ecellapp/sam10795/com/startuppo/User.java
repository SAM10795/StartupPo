package ecellapp.sam10795.com.startuppo;

/**
 * Created by SAM10795 on 03-03-2017.
 */

public class User {
    private String name;
    private String rollno;
    private int score;
    private int wins;
    private String hostel;

    public int getScore() {
        return score;
    }

    public String getHostel() {
        return hostel;
    }

    public int getWins() {
        return wins;
    }

    public String getName() {
        return name;
    }

    public String getRollno() {
        return rollno;
    }

    public void setHostel(String hostel) {
        this.hostel = hostel;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
