package webconn;

import java.io.IOException;

public interface IServerClientRunner {
    public void server() throws IOException;
    public void client() throws IOException;
}
