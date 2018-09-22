package util;

import android.content.Context;

import com.brian.napolicafe.views.activities.MainActivity;
import com.brian.napolicafe.views.activities.NotificationActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtility {

    /**
     * Write to Internal Storage
     *
     * @see MainActivity
     * @return void
     */
    public static void writeToFile(Context context, String data) {
        try {
            FileOutputStream outputStreamWriter
                    = context.getApplicationContext().openFileOutput("file", context.MODE_PRIVATE);
            outputStreamWriter.write(data.getBytes());
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read from Internal Storage
     *
     * @see NotificationActivity
     * @return String
     */
    public static String readFromFile(Context context) {
        String result = "";

        try {
            InputStream inputStream = context.openFileInput("myFile");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = null;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                result = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
