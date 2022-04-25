import lombok.Getter;
import lombok.Setter;

import java.util.*;
@Getter @Setter
public class User {
    // TODO: 12.04.2022 resolve problem with fillArrays(), make it more automatic
         Scanner s = new Scanner(System.in);
         String email;
         String password;
         String firstname;
         String lastname;
         int form;

    public  User(String email, String password, String firstname, String lastname, int form) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.form = form;
    }
    public User(){}



    public void fillArrays() {

        }
    }


