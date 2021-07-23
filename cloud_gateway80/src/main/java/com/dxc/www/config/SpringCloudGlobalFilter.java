package com.dxc.www.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.springframework.cloud.gateway.filter.GlobalFilter;

@Component
public class SpringCloudGlobalFilter implements GlobalFilter, Ordered {
    /**过滤业务逻辑*/
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求参数
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        if (token == null){
            //获取响应对象
            ServerHttpResponse response = exchange.getResponse();
            //响应类型
            response.getHeaders().add("Content-Type","application/json; charset=utf-8");
            //响应状态码，HTTP 401 错误代表用户没有访问权限
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //响应内容
            String message = "{\"message\":\""+HttpStatus.UNAUTHORIZED.getReasonPhrase()+"\"}";
            DataBuffer buffer = response.bufferFactory().wrap(message.getBytes());
            //请求结束，不再继续向下请求
            return response.writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }
    /**过滤器执行顺序，数值越小，优先级越高*/
    public int getOrder() {
        return 0;
    }
}