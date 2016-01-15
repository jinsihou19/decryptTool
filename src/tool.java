import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by vito on 15/11/9.
 */
public class tool {
    private JTabbedPane tab1;
    private JPanel panel1;
    private JTextField tab1url;
    private JButton openFile;
    private JTextArea tab1TextArea;
    private JButton tab1SaveBtn;
    private JButton tab1CancelBtn;
    private JTextField tab2Url;
    private JButton tab2OpenFile;
    private JButton tab2SaveBtn;
    private JButton tab2CancelBtn;
    private JTextArea tab2TextArea;
    private JCheckBox tab2CheckBox;
    private File tempFile = null;
    private File configFile = null;

    public tool() {
        tab1SaveBtn.setEnabled(false);
        tab2SaveBtn.setEnabled(false);
        tab1TextArea.setDragEnabled(true);
        tab1TextArea.setTransferHandler(new TransferHandler() {
            private static final long serialVersionUID = 1L;

            public boolean importData(JComponent c, Transferable t) {
                try {
                    List<File> fileList = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    File file = fileList.get(0);
                    if (file.isFile()) {
                        if (!file.getName().endsWith(".xml")) {
                            tempFile = file;
                            tab1url.setText(file.getAbsolutePath());
                            String text = DecryptTool.decryptToString(file.getAbsoluteFile());
                            tab1TextArea.setText(text);
                            tab1SaveBtn.setEnabled(true);
                        } else {
                            tab1TextArea.setText("�����ļ���ʹ�ò�������ļ��鿴��");
                        }
                    }
                    return true;
                } catch (UnsupportedFlavorException ufe) {
                    ufe.printStackTrace();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            public boolean canImport(JComponent c, DataFlavor[] flavors) {
                return true;
            }
        });

        //Tab1���ݿ�ʼ
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileHidingEnabled(false);
                FileFilter fileFilter = new FileNameExtensionFilter("timeTable file", "info");
                jfc.addChoosableFileFilter(fileFilter);
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showDialog(new JLabel(), "��");
                File file = jfc.getSelectedFile();
                if (file.isFile()) {
                    if (!file.getName().endsWith(".xml")) {
                        tempFile = file;
                        tab1url.setText(file.getAbsolutePath());
                        String text = DecryptTool.decryptToString(file.getAbsoluteFile());
                        tab1TextArea.setText(text);
                        tab1SaveBtn.setEnabled(true);
                    } else {
                        tab1TextArea.setText("�����ļ���ʹ�ò�������ļ��鿴��");
                    }
                }
            }
        });
        tab1SaveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tempFile != null) {
                    if (DecryptTool.encryptForString(tab1TextArea.getText(), tempFile)) {
                        tab1TextArea.setText("����ɹ���");
                    } else tab1TextArea.setText("����ʧ�ܣ�");
                    tab1SaveBtn.setEnabled(false);
                    tempFile = null;
                }
            }

        });

        tab1CancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tab1TextArea.setText("");
                tempFile = null;
                tab1SaveBtn.setEnabled(false);
                tab1url.setText("");
            }
        });
        //Tab1���ݽ���

        //������Tab2������
        tab2TextArea.setDragEnabled(true);
        tab2TextArea.setTransferHandler(new TransferHandler() {
            private static final long serialVersionUID = 1L;
            public boolean importData(JComponent c, Transferable t) {
                try {
                    List<File> fileList = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    File file = fileList.get(0);
                    if (file.isFile()) {
                        configFile = file;
                        tab2Url.setText(file.getAbsolutePath());
                        String text = DecryptTool.readPluginConfig(file);
                        if (DecryptTool.configIsLock(text)) {
                            text = DecryptTool.decryptConfig(text);

                            tab2CheckBox.setSelected(true);
                        }
                        tab2TextArea.setText(text);
                        tab2SaveBtn.setEnabled(true);
                    }
                    return true;
                } catch (UnsupportedFlavorException ufe) {
                    ufe.printStackTrace();
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            public boolean canImport(JComponent c, DataFlavor[] flavors) {
                return true;
            }
        });

        tab2OpenFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileHidingEnabled(false);
                FileFilter fileFilter = new FileNameExtensionFilter("pluginConfig file", "xml");
                jfc.addChoosableFileFilter(fileFilter);
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showDialog(new JLabel(), "�򿪲�������ļ�");
                File file = jfc.getSelectedFile();
                if (file.isFile()) {
                    configFile = file;
                    tab2Url.setText(file.getAbsolutePath());
                    String text = DecryptTool.readPluginConfig(file);
                    if (DecryptTool.configIsLock(text)) {
                        text = DecryptTool.decryptConfig(text);

                        tab2CheckBox.setSelected(true);
                    }
                    tab2TextArea.setText(text);
                    tab2SaveBtn.setEnabled(true);
                }
            }
        });

        tab2SaveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DecryptTool.outConfigFile(tab2TextArea.getText(), tab2CheckBox.isSelected(), configFile)) {
                    tab2TextArea.setText("����ɹ���");
                } else tab2TextArea.setText("����ʧ�ܣ�");
                tab2SaveBtn.setEnabled(false);
                configFile = null;
            }
        });

        tab2CancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tab2TextArea.setText("");
                configFile = null;
                tab2SaveBtn.setEnabled(false);
                tab2Url.setText("");
            }
        });
        //Tab2���ݽ���
    }

    public static void main(String[] args) {
        new tool().init();
    }

    public void init() {
        JFrame frame = new JFrame("����");
        frame.setContentPane(new tool().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
