package HttpClient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by ZJ on 2020/8/19
 * comment:
 */
public class PostTest {
    public static void main(String[] args) throws IOException {
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建HttpGet请求
        HttpPost httpPost = new HttpPost("http://www.itcast.cn/");
        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求
            response = httpClient.execute(httpPost);

            //判断响应状态码是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                //如果为200表示请求成功，获取返回数据
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                //打印数据长度
                System.out.println(content);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放连接
            if (response == null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                httpClient.close();
            }
        }
    }
}
