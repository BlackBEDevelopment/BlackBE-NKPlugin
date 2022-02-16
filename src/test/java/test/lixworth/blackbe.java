package test.lixworth;

import com.google.gson.Gson;
import icu.cattery.lixworth.blackbe.KickRunnable;
import icu.cattery.lixworth.blackbe.entity.Api;
import icu.cattery.lixworth.blackbe.BlackBE;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Test Class
 * @author LixWorth
 * @create 2021/9/15 19:50
 */
public class blackbe {
    public static void main(String[] args) {
        try {
            URL url = new URL(BlackBE.api_domain + "/v3/check?name=" + URLEncoder.encode("blackbetest","UTF-8")+"&xuid="+URLEncoder.encode("2333","UTF-8"));
            System.out.println(url.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("User-Agent", "RuMao/1.3");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;

            while ((inputLine = bufferedReader.readLine()) != null) {
                Gson gson = new Gson();
                Api api = gson.fromJson(inputLine, Api.class);
                if (api.getExist()) {
                    System.out.println("有");
                } else {
                    System.out.println("无云黑记录，正常进入");
                }
            }
            bufferedReader.close();
            httpURLConnection.disconnect();
            // IOException MalformedURLException UnsupportedEncodingException
        } catch (Exception exception) {
            System.out.println("云黑插件出现错误 请稍后重试 "+exception.getMessage()+" @ "+exception.getClass());
        }
    }
}
