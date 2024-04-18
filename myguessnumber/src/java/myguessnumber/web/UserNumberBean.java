package myguessnumber.web;

//import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@Named
@ManagedBean(name = "UserNumberBean")
@SessionScoped
public class UserNumberBean implements Serializable {

    Integer randomInt = null;
    private Integer userNumber = null;
    String response = null;
    private int maximum = 100;
    private int minimum = 0;
    private List<Integer> guesses = new ArrayList<>();

    public UserNumberBean() {
        Random randomGR = new Random();
        randomInt = randomGR.nextInt(maximum - minimum + 1) + minimum;
        // Print number to server log
        System.out.println("Duke's number: " + randomInt);
    }

    public void setUserNumber(Integer user_number) {
        userNumber = user_number;
    }

    public Integer getUserNumber() {
        return userNumber;
    }

    public String getResponse() {
        if (userNumber != null) {
            int hint = this.getHint(userNumber);
            if (hint == 0) {
                // Invalidate user session
                // (Session may have expired due to timeout. In this case no need to do anything.)
                return "Yay! You got it!";
            } else {
                // Record guess history
                synchronized (this) {
                    guesses.add(userNumber);
                }

                return "Sorry, " + userNumber + " is incorrect. My number is "
                        + (hint < 0 ? "smaller." : "larger.");
            }
        } else {
            return "Please enter a number.";
        }
    }

    public int getMaximum() {
        return this.maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public int getMinimum() {
        return this.minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }
    
    // Return -1, 0 or 1 if the Duke's number is smaller than, equal to or 
    // larger than the guess 
    public int getHint(Integer guess) {
        return randomInt.compareTo(guess);
    }

    public synchronized List<Integer> getGuesses() {
        List<Integer> results = new ArrayList<>();
        results.addAll(this.guesses);

        return results;
    }
}