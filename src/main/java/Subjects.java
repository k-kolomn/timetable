import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class Subjects {
    String name;

    public Subjects(String name) {
        this.name = name;
    }

}
