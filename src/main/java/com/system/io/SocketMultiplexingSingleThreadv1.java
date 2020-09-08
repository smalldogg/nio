package com.system.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wangyh
 * @create 2020-09-07 19:39
 */

public class SocketMultiplexingSingleThreadv1 {
    private ServerSocketChannel server = null;
    private Selector selector = null;
    private static final int port = 9090;

    public void init() throws Exception {
        server = ServerSocketChannel.open();
        server.configureBlocking(false); //设置为非阻塞
        server.bind(new InetSocketAddress(port));
        selector = Selector.open();//如果在epoll模型下，open--》  epoll_create -> fd3
            /*
            register
            如果：
            select，poll：jvm里开辟一个数组 fd4 放进去
            epoll：  epoll_ctl(fd3,ADD,fd4,EPOLLIN
             */
        server.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws Exception {
        init();
        System.out.println("服务器启动了");
        while (true) {
            while (selector.select(500) > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                //调用多路复用器(select,poll  or  epoll  (epoll_wait))
                /*
                select()是啥意思：
                1，select，poll  其实  内核的select（fd4）  poll(fd4)
                2，epoll：  其实 内核的 epoll_wait()
                *, 参数可以带时间：没有时间，0  ：  阻塞，有时间设置一个超时
                selector.wakeup()  结果返回0
                懒加载：
                其实再触碰到selector.select()调用的时候触发了epoll_ctl的调用
                 */
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        acceptHandler(key);
                    } else {
                        readHandler(key);
                    }
                }
            }
        }
    }


    public void acceptHandler(SelectionKey key) throws Exception {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel client = ssc.accept(); //来啦，目的是调用accept接受客户端  fd7
        client.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(8192);  //前边讲过了
        // 0.0  我类个去
        //你看，调用了register
            /*
            select，poll：jvm里开辟一个数组 fd7 放进去
            epoll：  epoll_ctl(fd3,ADD,fd7,EPOLLIN
             */
        client.register(selector, SelectionKey.OP_READ, buffer);
        System.out.println("-------------------------------------------");
        System.out.println("新客户端：" + client.getRemoteAddress());
        System.out.println("-------------------------------------------");
    }


    public void readHandler(SelectionKey key) throws Exception {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read = 0;
        while (true) {
            read = client.read(buffer);
            if (read > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) client.write(buffer);
                buffer.clear();
            } else if (read < 0) {
                client.close();
                break;
            } else {
                break;
            }
        }
    }


    public static void main(String[] args) throws Exception {
        SocketMultiplexingSingleThreadv1 socket = new SocketMultiplexingSingleThreadv1();
        socket.start();
    }
}
