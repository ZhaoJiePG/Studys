package HttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ZJ on 2020/8/20
 * comment:
 */
public class PostParam {
    public static void main(String[] args) throws IOException {
        //创建HttpClient对象
        CloseableHttpClient client = HttpClients.createDefault();

        //创建HttpPost请求
        HttpPost httpPost = new HttpPost("http://ww.itcast.cn/");

        //请求参数放入集合中
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicHeader("keys","java"));

        //创建表单数据
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "UTF-8");

        //设置entity到httpPost的请求对象中
        httpPost.setEntity(urlEncodedFormEntity);

        CloseableHttpResponse response = null;
        try {
            //使用HttpClient发起请求
            response = client.execute(httpPost);

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
                client.close();
            }
        }

    }
}
