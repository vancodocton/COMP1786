package uk.ac.gre.nt4738f.comp1786.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.R;
import uk.ac.gre.nt4738f.comp1786.core.payloads.UploadedExpense;
import uk.ac.gre.nt4738f.comp1786.core.payloads.UploadedPayload;
import uk.ac.gre.nt4738f.comp1786.infrastructure.TripDbHelper;

public class UploadActivity extends AppCompatActivity {

    private TripDbHelper dbHelper;
    private HttpURLConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        dbHelper = new TripDbHelper(getApplicationContext());

        Button uploadBtn = findViewById(R.id.buttonUpload);
        uploadBtn.setOnClickListener(view -> {
            String payload = getUploadedPayload();
            String uploadUrl = getString(R.string.server_origin) + "/COMP1424CW";
            UploadThread myTask = new UploadThread(this, uploadUrl, payload);
            Thread t1 = new Thread(myTask, "JSON Thread");
            t1.start();
        });
    }

    private String getUploadedPayload() {
        ArrayList<UploadedExpense> uploadedExpenses = dbHelper.listExpenseUploads();

        String userId = "12312312";
        UploadedPayload payload = new UploadedPayload(userId, uploadedExpenses);

        Gson gson = new Gson();
        return gson.toJson(payload);
    }


    static class UploadThread implements Runnable {
        private final Activity activity;
        private final String url;
        private HttpURLConnection connection;
        private final String payload;
        private String response;

        UploadThread(@NotNull Activity activity, @NotNull String url, @NotNull String payload) {
            this.activity = activity;
            this.url = url;
            this.payload = payload;
        }

        @Override
        public void run() {
            Result result = prepareHttpPostRequest();

            if (!result.isSucceed) {
                showResult(result);
                return;
            }
            result = setRequestBody();

            if (!result.isSucceed) {
                showResult(result);
                return;
            }

            result = postJsonPayload();
            if (!result.isSucceed) {
                showResult(result);
            } else connection.disconnect();

            showResult(Result.succeed);
        }

        private void showResult(Result result) {
            activity.runOnUiThread(() -> {
                if (result.isSucceed) new AlertDialog.Builder(activity)
                        .setTitle("Upload successful")
                        .setMessage(response)
                        .show();
                else new AlertDialog.Builder(activity)
                        .setTitle(result.message)
                        .setMessage(result + "\n" + result.exception.toString())
                        .setNeutralButton("Back", null)
                        .show();
            });
        }

        private Result prepareHttpPostRequest() {
            try {
                URL uploadUrl = new URL(url);
                connection = (HttpURLConnection) uploadUrl.openConnection();

                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                return Result.succeed;
            } catch (Exception e) {
                e.printStackTrace();
                return Result.failed("Cannot prepare the request", e);
            }
        }

        private Result setRequestBody() {
            try {
                String data = URLEncoder.encode("jsonpayload", "UTF-8")
                        + "=" + URLEncoder.encode(payload, "UTF-8");

                connection.setFixedLengthStreamingMode(data.getBytes().length);

                PrintWriter out = new PrintWriter(connection.getOutputStream());
                out.print(data);
                out.close();
                return Result.succeed;
            } catch (Exception e) {
                e.printStackTrace();
                return Result.failed("Cannot set Request Body", e);
            }
        }

        private Result postJsonPayload() {
            try {
                int responseCode = connection.getResponseCode();
                Result result = readResponseFromStream(connection.getInputStream());
                if (!result.isSucceed) {
                    return result;
                }
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    return Result.succeed("Success with status code: " + responseCode);
                } else {
                    return Result.failed("Failed with status code: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return Result.failed("Cannot post Json Payload");
            }
        }

        private Result readResponseFromStream(InputStream in) {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String nextLine;
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return Result.failed("Cannot read response from stream", e);
            }
            response = sb.toString();
            return Result.succeed;
        }
    }

    public static class Result {
        public final boolean isSucceed;
        private final String message;
        public final Exception exception;


        private Result(boolean isSucceed, String message, Exception exception) {
            this.isSucceed = isSucceed;
            this.message = message;
            this.exception = exception;
        }

        public static Result succeed = new Result(true, null, null);

        public static Result succeed(String message) {
            return new Result(true, message, null);
        }

        public static Result failed(String message, Exception e) {
            return new Result(false, message, e);
        }

        public static Result failed(String message) {
            return new Result(false, message, null);
        }
    }
}