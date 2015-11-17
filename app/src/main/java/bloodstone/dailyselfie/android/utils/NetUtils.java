package bloodstone.dailyselfie.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
        /*HttpClient client = new DefaultHttpClient();
        Uri endpoint=Uri.parse(url);
        Uri.Builder builder=new Uri.Builder();
        builder.authority(url);
        for(String key:params.keySet()){
            builder.appendQueryParameter(key,params.get(key));
        }*/
        //HttpPost post=new HttpPost(builder.build().toString());

        /*HttpPost post=new HttpPost("http://dailyselfiecloud-env.elasticbeanstalk.com/applyeffect?type=3&userid=user1");
        post.addHeader("Accept", "multipart/form-data");
        post.addHeader("Content-Type", "image/jpeg");
        MultipartEntityBuilder entityBuilder=MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        entityBuilder.addBinaryBody("file", fileStream);
        HttpEntity entity=entityBuilder.build();

        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        HttpEntity httpEntity = response.getEntity();
        InputStream stream=httpEntity.getContent();*/

        InputStream stream=PostMultiPart.multipartRequest("http://dailyselfiecloud-env.elasticbeanstalk.com/applyeffect",params
        ,fileStream,"file","image/jpeg");

        return stream;

    }
}
