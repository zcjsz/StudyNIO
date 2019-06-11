package mynio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {

    public static void main(String[] args) {

        RandomAccessFile aFile = null;
        RandomAccessFile bFile = null;
        FileChannel channelA = null;
        FileChannel channelB = null;

        try {
            aFile = new RandomAccessFile("./src/data/TestFile.txt", "r");
            bFile = new RandomAccessFile("./src/data/OutBFile.txt", "rw");

            channelA = aFile.getChannel();
            channelB = bFile.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(10);

            while(channelA.read(buffer) != -1) {
                buffer.flip();
                channelB.write(buffer);
                buffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
               if(channelA != null) channelA.close();
               if(channelB != null) channelB.close();
               if(aFile != null) aFile.close();
               if(bFile != null) bFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

}
