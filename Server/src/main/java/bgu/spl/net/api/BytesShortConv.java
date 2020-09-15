package bgu.spl.net.api;

public class BytesShortConv {

    private static class SingletonHolder{
        private static BytesShortConv instance = new BytesShortConv();
    }
    /**
     *This Class returns a new instance of a Convector Number from bytes to Short and the opposite
     */
    private BytesShortConv(){}

    public static BytesShortConv getInstance(){return SingletonHolder.instance;}

    /**
     * @return short num from byte array
     */

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }
    /**
     * @return byte Array from Short num
     */

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }


}
