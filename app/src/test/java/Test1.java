import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Test1 {
    public static void main(String[] args) {
        final String messageTimestamps = "1654869348590";
        Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));

        Date date = new Date(timestamp.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

//        simpleDateFormat.setTimeZone(TimeZone.getDefault());

        System.out.println(simpleTimeFormat.format(date));

    }

}
