import com.fr.general.DesUtils;
import com.fr.general.IOUtils;

import java.io.*;

/**
 * Created by vito on 15/11/9.
 */
public class DecryptTool {

    /**
     * ������ַ���
     *
     * @param xmlLike xml�����ļ�
     * @return String ����
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
     * �����ַ��������
     *
     * @param text     Ҫ���ܵ��ı�
     * @param tempfile Ŀ���ļ�
     * @return
     */
    public static boolean encryptForString(String text, File tempfile) {
        String resultText;
        resultText = DesUtils.encodeWithHex(text);
        return writeToFile(resultText, tempfile);
    }

    /**
     * ��ȡ��������ļ�
     *
     * @param xmlLike xml�����ļ�
     * @return String �õ�������
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
     * �Ƿ����
     *
     * @param text ��������ļ�����
     * @return �Ƿ����
     */
    public static boolean configIsLock(String text) {
        return text.contains("<FREncrypted>");
    }

    /**
     * �Լ��ܵĲ�������ļ����н���
     *
     * @param text ��������ļ�����
     * @return ���ܽ��
     */
    public static String decryptConfig(String text) {
        text = text.replaceAll("<FREncrypted>", "");
        return DesUtils.decodeWithHex(text);
    }

    /**
     * ��������ļ�
     *
     * @param text       �����������
     * @param isNeedLock �Ƿ����
     * @param file       Ŀ���ļ�
     * @return
     */
    public static boolean outConfigFile(String text, boolean isNeedLock, File file) {
        if (isNeedLock) {
            text = "<FREncrypted>" + DesUtils.encodeWithHex(text);
        }
        return writeToFile(text, file);
    }

    /**
     * ������ļ���
     *
     * @param text �����������
     * @param file Ŀ���ļ�
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
