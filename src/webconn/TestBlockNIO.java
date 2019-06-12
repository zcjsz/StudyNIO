package webconn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/***
 * 一、使用 NIO 完成网络通信的三个核心：
 * 1. 通道(Channel)：负责连接
 *
 *      java.nio.channels.Channel 接口：
 *          |-- SelectableChannel
 *              |-- SocketChannel (TCP)
 *              |-- ServerSocketChannel (TCP)
 *              |-- DatagramChannel (UDP)
 *
 * 2. 缓冲区(Buffer)：负责数据的存取
 * 3. 选择器(Selector)：是 SelectableChannel 的多路复用器，用于监控 SelectableChannel 的 IO 状况
 */

public class TestBlockNIO implements IServerClientRunner {

    @Override
    public void client() throws IOException {

        // 1. 建立 FileChannel
        FileChannel fileChannel = FileChannel.open(Paths.get("./src/data/TestFile.txt"), StandardOpenOption.READ);

        // 2. 分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 3. 建立 SocketChannel
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        // 4. 读取本地文件，并发送到服务端
        while(fileChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        socketChannel.shutdownOutput();

        // 5. 接收服务端反馈
        int len = 0;
        while((len = socketChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            System.out.println(new String(byteBuffer.array(), 0, len));
            byteBuffer.clear();
        }

        fileChannel.close();
        socketChannel.close();
        
    }

    @Override
    public void server() throws IOException {

        // 1. 建立 FileChannel
        FileChannel fileChannel = FileChannel.open(Paths.get("./src/data/OutAFile.txt"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        // 2. 分配缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        // 3. 建立 ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 4. 绑定端口号
        serverSocketChannel.bind(new InetSocketAddress(9898));

        // 5. 获取客户端连接的通道
        SocketChannel socketChannel = serverSocketChannel.accept();

        // 6. 接收客户端数据并保存
        while(socketChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        // 7. 反馈信息给客户端
        byteBuffer.put("Done!".getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);

        socketChannel.shutdownOutput();

        fileChannel.close();
        socketChannel.close();
        serverSocketChannel.close();

    }


    public static void main(String[] args) throws IOException, InterruptedException {

        TestBlockNIO testBlockNIO = new TestBlockNIO();

        ServerRunner serverRunner = new ServerRunner(testBlockNIO);
        ClientRunner clientRunner = new ClientRunner(testBlockNIO);

        Thread sth = new Thread(serverRunner);
        Thread cth = new Thread(clientRunner);

        sth.start();

        Thread.sleep(1000);

        cth.start();
    }


}
