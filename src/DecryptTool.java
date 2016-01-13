import com.fr.general.DesUtils;
import com.fr.general.IOUtils;

import java.io.*;

/**
 * Created by vito on 15/11/9.
 */
public class DecryptTool {

    /**
     * 解码成字符串
     *
     * @param xmlLike xml类型文件
     * @return String 内容
     */
    public static String decryptToString(File xmlLike) {
        String resultText = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(xmlLike);
            String encodeText = IOUtils.inputStream2String(fileInputStream, "UTF-8");
            fileInputStream.close();
            resultText = DesUtils.decodeWithHex(encodeText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultText == null ? "" : resultText;
    }

    /**
     * 加密字符串后输出
     *
     * @param text     要加密的文本
     * @param tempfile 目标文件
     * @return
     */
    public static boolean encryptForString(String text, File tempfile) {
        String resultText;
        resultText = DesUtils.encodeWithHex(text);
        return writeToFile(resultText, tempfile);
    }

    /**
     * 读取插件配置文件
     *
     * @param xmlLike xml类型文件
     * @return String 得到的内容
     */
    public static String readPluginConfig(File xmlLike) {
        try {
            FileInputStream fileInputStream = new FileInputStream(xmlLike);
            return IOUtils.inputStream2String(fileInputStream, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否加密
     *
     * @param text 插件配置文件内容
     * @return 是否加密
     */
    public static boolean configIsLock(String text) {
        return text.contains("<FREncrypted>");
    }

    /**
     * 对加密的插件配置文件进行解密
     *
     * @param text 插件配置文件内容
     * @return 解密结果
     */
    public static String decryptConfig(String text) {
        text = text.replaceAll("<FREncrypted>", "");
        return DesUtils.decodeWithHex(text);
    }

    /**
     * 输出配置文件
     *
     * @param text       待处理的内容
     * @param isNeedLock 是否加密
     * @param file       目标文件
     * @return
     */
    public static boolean outConfigFile(String text, boolean isNeedLock, File file) {
        if (isNeedLock) {
            text = "<FREncrypted>" + DesUtils.encodeWithHex(text);
        }
        return writeToFile(text, file);
    }

    /**
     * 输出到文件中
     *
     * @param text 待输出的内容
     * @param file 目标文件
     * @return
     */
    private static boolean writeToFile(String text, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(text);
            bufferedWriter.close();
            outputStreamWriter.close();
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
