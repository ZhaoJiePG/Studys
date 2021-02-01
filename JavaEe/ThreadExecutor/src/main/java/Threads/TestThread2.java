package Threads;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by ZJ on 2021/1/23
 * comment:
 */

//练习Thread，实现多线程同步下载图片
public class TestThread2 extends Thread{
    private String url;//网络图片url
    private String name;//保存文件名

    public TestThread2(String url,String name){
        this.url = url;
        this.name = name;
    }

    @Override
    public void run() {
        WebDownLoader webDownLoader = new WebDownLoader();
        webDownLoader.downloader(url,name);
        System.out.println(name + "文件下载完成");
    }

    public static void main(String[] args) {
        TestThread2 t1 = new TestThread2("http://files.jb51.net/file_images/article/201409/git_big_jb51.jpg", "./url1.txt");
        TestThread2 t2 = new TestThread2("http://files.jb51.net/file_images/article/201409/git_big_jb51.jpg", "./url2.txt");
        TestThread2 t3 = new TestThread2("http://files.jb51.net/file_images/article/201409/git_big_jb51.jpg", "./url3.txt");

        t1.start();
        t2.start();
        t3.start();
    }
}

//下载器
class WebDownLoader{
    //下载方法
    public void downloader(String url,String name){

        try {
            FileUtils.copyURLToFile(new URL(url), new File(name));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IO异常，downloader方法出现问题");
        }

    }
}