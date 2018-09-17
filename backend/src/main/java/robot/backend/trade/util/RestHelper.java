package robot.backend.trade.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestHelper {

    public <T> T mapDtoFromUrl(String url, Class<T> classT) throws IOException {
        T retVal;
        HttpURLConnection httpcon = (HttpURLConnection) new URL(url).openConnection();
        httpcon.setReadTimeout(4000);
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");

        InputStream bais = cloneAndPrintSteam(httpcon.getInputStream(), url);
        InputStreamReader reader = new InputStreamReader(bais);

        if (!classT.isArray()) {
            retVal = new Gson().fromJson(reader, classT);
        } else {
            Type itemsArrType = new TypeToken<T>() {}.getType();
            retVal = new Gson().fromJson(reader, itemsArrType);
        }
        return retVal;
    }

    public <T> T mapDtoFromUrlPost(String url, String request, Class<T> classT) throws IOException {
        T retVal;
        HttpURLConnection httpcon = (HttpURLConnection) new URL(url).openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.76");
        httpcon.setReadTimeout(4000);
        httpcon.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpcon.setDoOutput(true);
        httpcon.setRequestMethod("POST");

        System.out.println("Request from " + url + " : " + request);
        OutputStream os = httpcon.getOutputStream();
        os.write(request.getBytes());
        os.close();

        InputStream bais = cloneAndPrintSteam(httpcon.getInputStream(), url);
        InputStreamReader reader = new InputStreamReader(bais);

        if (!classT.isArray()) {
            retVal = new Gson().fromJson(reader, classT);
        } else {
            Type itemsArrType = new TypeToken<T>() {}.getType();
            retVal = new Gson().fromJson(reader, itemsArrType);
        }
        return retVal;
    }

    private InputStream cloneAndPrintSteam(InputStream inputStream, String url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        org.apache.commons.io.IOUtils.copy(inputStream, baos);
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        System.out.print("Response from " + url + " : ");
        printStream(bais);
        bais = new ByteArrayInputStream(bytes);
        bais.reset();

        return bais;
    }

    private void printStream(InputStream inputStream) throws IOException {
        String readLine;
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        while (((readLine = br.readLine()) != null)) {
            System.out.println(readLine);
        }
    }
}
