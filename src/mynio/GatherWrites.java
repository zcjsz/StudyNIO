package mynio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class GatherWrites {

    public static void main(String[] args) {

        RandomAccessFile aFile = null;
        FileChannel channelA = null;

        RandomAccessFile cFile = null;
        FileChannel channelC = null;

        try {

            aFile = new RandomAccessFile("./src/data/TestFile.txt", "rw");
            cFile = new RandomAccessFile("./src/data/OutCFile.txt", "rw");

            channelA = aFile.getChannel();
            channelC = cFile.getChannel();

            ByteBuffer buf1 = ByteBuffer.allocate(10);
            ByteBuffer buf2 = ByteBuffer.allocate(10);
            ByteBuffer buf3 = ByteBuffer.allocate(10);

            ByteBuffer[] bufs = {buf1, buf2, buf3};

            channelA.read(bufs);

            for(ByteBuffer buf : bufs) {
                buf.flip();
            }

            channelC.write(bufs);

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if(channelA != null) channelA.close();
                if(channelC != null) channelC.close();
                if(aFile != null) aFile.close();
                if(cFile != null) cFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
