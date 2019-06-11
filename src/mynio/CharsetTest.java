package mynio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map.Entry;
import java.util.SortedMap;

public class CharsetTest {

    public static void main(String[] args) {

        SortedMap<String, Charset> stringCharsetSortedMap = Charset.availableCharsets();
        for(Entry<String, Charset> entry : stringCharsetSortedMap.entrySet()) {
            System.out.println(entry.getKey() + " --- " + entry.getValue());
        }


        CharBuffer charBuf1 = CharBuffer.allocate(256);
        charBuf1.put("Charset测试方法");
        charBuf1.flip();

        Charset gbk = Charset.forName("GBK");
        CharsetEncoder gbkEncoder = gbk.newEncoder();
        CharsetDecoder gbkDecoder = gbk.newDecoder();

        Charset utf8 = Charset.forName("UTF-8");
        CharsetEncoder utf8Encoder = utf8.newEncoder();
        CharsetDecoder utf8Decoder = utf8.newDecoder();

        try {

            ByteBuffer byteBuf = gbkEncoder.encode(charBuf1);
            for (int i = 0; i <byteBuf.limit() ; i++) {
                System.out.println(byteBuf.get(i));
            }

            CharBuffer charBuf2 = gbk.decode(byteBuf);
            System.out.println(charBuf2.toString());

            System.out.println("==================================");

            byteBuf.flip();

            CharBuffer charBuf3 = utf8.decode(byteBuf);
            System.out.println(charBuf3.toString());

        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }

    }

}
