package mynio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ScatterReads {

    public static void main(String[] args) {

        RandomAccessFile aFile = null;
        FileChannel channelA = null;

        try {

            aFile = new RandomAccessFile("./src/data/TestFile.txt", "r");
            channelA = aFile.getChannel();

            ByteBuffer buf1 = ByteBuffer.allocate(10);
            ByteBuffer buf2 = ByteBuffer.allocate(10);
            ByteBuffer buf3 = ByteBuffer.allocate(10);

            ByteBuffer[] bufs = {buf1, buf2, buf3};

            channelA.read(bufs);

            for(ByteBuffer buf : bufs) {
                buf.flip();
                System.out.println(new String(buf.array(), 0, buf.limit()));
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if(channelA != null) channelA.close();
                if(aFile != null) aFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
