package com.rxt.common.trafficLimiting;

import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * guava 令牌桶、漏桶
 */
public class Token {

    //bursty(令牌桶)
    RateLimiter rateLimiter = RateLimiter.create(10); //qps是10

//    // 漏桶
//    RateLimiter rateLimiter = RateLimiter.create(1000, 10, TimeUnit.MILLISECONDS);

    public void doPay() {
        if (rateLimiter.tryAcquire()) { //尝试去获取一个令牌
            System.out.println(Thread.currentThread().getName() + " 开始执行支付");
        } else {
            System.out.println("系统繁忙");
        }
    }

    public static void main(String[] args) throws IOException {
        Token token = new Token();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();    //使当前线程阻塞
                    Thread.sleep(random.nextInt(1000));
                    token.doPay();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        countDownLatch.countDown();
        System.in.read();
    }

}
