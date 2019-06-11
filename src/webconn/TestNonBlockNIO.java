package webconn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class TestNonBlockNIO {

    public void client() throws IOException {

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

        // 5. 关闭通道
        socketChannel.close();
    }


    public void server() {

        // 1. 分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 2. 建立 ServerSocketChannel


        // 3. 切换非阻塞模式


        // 4. 绑定端口号


        // 5. 获取选择器


        // 6. 将通道注册到选择器上，并指定监听事件


        // 7. 轮询选择器上就绪的事件
    }


}
