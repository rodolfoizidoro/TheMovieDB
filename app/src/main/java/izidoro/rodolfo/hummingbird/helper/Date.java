package izidoro.rodolfo.hummingbird.helper;


import java.text.ParseException;
import java.text.SimpleDateFormat;


public class Date {

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        java.util.Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
        }

        return outputDate;

    }

}