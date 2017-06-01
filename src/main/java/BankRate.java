import com.google.gson.annotations.SerializedName;

/**
 * Created by estaine on 31.05.2017.
 */
public class BankRate {

    @SerializedName("Date")
    private String date;

    @SerializedName("Value")
    private Double value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
