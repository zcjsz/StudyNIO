package webconn;

import java.io.IOException;

class ServerRunner implements Runnable {
    private IServerClientRunner serverRunner = null;
    ServerRunner(IServerClientRunner serverRunner) {
        this.serverRunner = serverRunner;
    }
    @Override
    public void run() {
        try {
            System.out.println("server start");
            serverRunner.server();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class ClientRunner implements Runnable {
    private IServerClientRunner clientRunner = null;
    ClientRunner(IServerClientRunner clientRunner) {
        this.clientRunner = clientRunner;
    }
    @Override
    public void run() {
        try {
            System.out.println("client start");
            Thread.sleep(3000); // 模拟客户端阻塞
            System.out.println("client ready");
            clientRunner.client();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
