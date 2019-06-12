package webconn;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class TestNonBlockNIO implements IServerClientRunner {


    public void client() throws IOException {

        System.out.println("TestNonBlockNIO Client Run");

        // 1. 分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 2. 建立 SocketChannel
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        // 3. 切换非阻塞模式
        socketChannel.configureBlocking(false);

        // 4. 发送数据给客户端
        byteBuffer.put(new Date().toString().getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);
        byteBuffer.clear();

        socketChannel.shutdownOutput();

        // 5. 关闭通道
        socketChannel.close();
    }


    public void server() throws IOException {

        System.out.println("TestNonBlockNIO Server Run");

        // 1. 分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 2. 建立 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 3. 切换非阻塞模式
        serverSocketChannel.configureBlocking(false);

        // 4. 绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(9898));

        // 5. 获取选择器
        Selector selector = Selector.open();

        // 6. 将通道注册到选择器上，并指定监听事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 7. 轮询选择器上就绪的事件

        while(true) {

             int readyChannels = selector.selectNow();
             if(readyChannels == 0) continue;

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if(key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel. 接入处理
                    // 1). 建立 SocketChannel 来对接刚接入的连接
                    // 2). 将 SocketChannel 修改为非阻塞模式
                    // 3). 将 SocketChannel 注册到选择器，并设置为可读可写
                    System.out.println("A socket connection is acceptable");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.
                } else if (key.isReadable()) {
                    // a channel is ready for reading. 可读事件处理
                    // 1). 获取 SelectionKey 对应的 SocketChannel
                    // 2). 对 SocketChannel 进行读操作
                    System.out.println("A socket channel is readable");
                    SocketChannel channel = (SocketChannel) key.channel();
                    readFromSocketChannel(channel);
                    channel.close();
                } else if (key.isWritable()) {
                    // a channel is ready for writing
                }
                keyIterator.remove();
            }
        }
    }


    private void readFromSocketChannel(SocketChannel socketChannel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int len = 0;
        while(true) {
            try {
                if (((len = socketChannel.read(byteBuffer)) == -1)) break;
                byteBuffer.flip();
                String msg = new String(byteBuffer.array(), 0, len);
                System.out.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeToSocketChannel() {

    }


    public static void main(String[] args) throws InterruptedException {

        TestNonBlockNIO testNonBlockNIO = new TestNonBlockNIO();

        ServerRunner serverRunner = new ServerRunner(testNonBlockNIO);
        ClientRunner clientRunner = new ClientRunner(testNonBlockNIO);

        Thread sth = new Thread(serverRunner);
        Thread cth = new Thread(clientRunner);

        sth.start();

        Thread.sleep(2000);

        cth.start();

    }


}



