package test;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HellWorld {
    public static void main(String[] args) throws IOException {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建HttpGet请求
        HttpGet httpGet = new HttpGet("http://www.itcast.cn/");

        CloseableHttpResponse response = null;

        try {
            //使用HttpClient发起请求
            response = httpClient.execute(httpGet);

            //判断相应状态码是否为200
            if(response.getStatusLine().getStatusCode() == 200){
                //如果为200表示请求成功，获取返回数据
                String context = EntityUtils.toString(response.getEntity(), "utf-8");
                //打印数据长度
                System.out.println(context);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放连接
            if(response == null){
                try {
                    response.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
                httpClient.close();
            }
        }
    }
}
