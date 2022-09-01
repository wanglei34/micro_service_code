package com.wl.consumer.api;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author 17997
 */
@RestController
public class ConsumerController {

    private final String SERVICE_URL = "http://9x5big.natappfree.cc";
    @Resource
    private RestTemplate restTemplate;

    private WebClient webClient = WebClient.builder()
            .baseUrl(SERVICE_URL)
            .build();

    @GetMapping("httpClient")
    public String httpClientTest() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建HTTP get请求
        HttpGet httpGet = new HttpGet(SERVICE_URL+"/a=3/b=4");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            System.out.println(response.getStatusLine().getStatusCode());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200){
                String res = EntityUtils.toString(response.getEntity(),"utf-8");
                System.out.println(res);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }finally {
            if (response != null){
                response.close();
            }
            httpClient.close();
        }
        return "请求成功！";
    }

    @GetMapping("restTemplate")
    public String restTemplateTest() {
        System.out.println(restTemplate.getForObject(SERVICE_URL+"/a=3/b=4",String.class));
        return restTemplate.getForObject(SERVICE_URL+"/a=2/b=6",String.class);
    }

    @GetMapping("/webClient")
    private String webClientTest(){
        Mono<String> mono = webClient.get().uri("/a=1/b=5").retrieve().bodyToMono(String.class);
        mono.subscribe(System.out::println);
        return "请求成功";
    }
}
