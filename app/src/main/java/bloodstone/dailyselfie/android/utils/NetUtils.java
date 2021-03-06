package bloodstone.dailyselfie.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by minsamy on 10/31/2015.
 */
public class NetUtils {
    private static final String TAG = NetUtils.class.getName();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mngr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mngr.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return true;
        }
        return false;
    }

    public static String post(String address, Map<String, String> headers, String requestBody) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            //set the headers
            connection.setRequestProperty("Content-Type","application/json");
            if (headers != null) {
                for (String header : headers.keySet()) {
                    connection.setRequestProperty(header, headers.get(header));
                }
            }

            if(requestBody!=null){
                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                OutputStreamWriter writer = new OutputStreamWriter(out);
                writer.write(requestBody, 0, requestBody.length());

                writer.close();
            }


            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();
            if (connection != null) {
                connection.disconnect();
            }

        } catch (MalformedURLException e) {
            LogUtil.logError(TAG, e.toString());
        } catch (IOException e) {
            LogUtil.logError(TAG, e.toString());
        }

        return buffer.toString();

    }

    static public InputStream postMultiPart(String url,HashMap<String,String>params,InputStream fileStream) throws IOException {
        InputStream stream=PostMultiPart.multipartRequest("http://dailyselfiecloud-env.elasticbeanstalk.com/applyeffect",params
        ,fileStream,"file","image/jpeg");
        return stream;

    }
}
