import com.fr.general.ComparatorUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.UIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.im.InputContext;
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
    private static String TAB1 = "tab1";
    private static String TAB2 = "tab2";

    public tool() {
        tab1SaveBtn.setEnabled(false);
        tab2SaveBtn.setEnabled(false);

        //Tab1内容开始
        tab1TextArea.setDragEnabled(true);
        tab1TextArea.setFont(new Font("微软雅黑", Font.PLAIN,14));
        tab1TextArea.setTransferHandler(new DefaultTransferHandler(TAB1));

        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileHidingEnabled(false);
                FileFilter fileFilter = new FileNameExtensionFilter("timeTable file", "info");
                jfc.addChoosableFileFilter(fileFilter);
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showDialog(new JLabel(), "打开");
                File file = jfc.getSelectedFile();
                doInTabOne(file);
            }
        });
        tab1SaveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tempFile != null) {
                    if (DecryptTool.encryptForString(tab1TextArea.getText(), tempFile)) {
                        tab1TextArea.setText("保存成功！");
                    } else tab1TextArea.setText("保存失败！");
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
        //Tab1内容结束

        //以下是Tab2的内容
        tab2TextArea.setDragEnabled(true);
        tab2TextArea.setFont(new Font("微软雅黑", Font.PLAIN,14));
        tab2TextArea.setTransferHandler(new DefaultTransferHandler(TAB2));

        tab2OpenFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileHidingEnabled(false);
                FileFilter fileFilter = new FileNameExtensionFilter("pluginConfig file", "xml");
                jfc.addChoosableFileFilter(fileFilter);
                jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                jfc.showDialog(new JLabel(), "打开插件配置文件");
                File file = jfc.getSelectedFile();
                doInTabTwo(file);
            }
        });

        tab2SaveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DecryptTool.outConfigFile(tab2TextArea.getText(), tab2CheckBox.isSelected(), configFile)) {
                    tab2TextArea.setText("保存成功！");
                } else tab2TextArea.setText("保存失败！");
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
        //Tab2内容结束
    }

    public void doInTabTwo(File file) {
        if (file != null && file.isFile()) {
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

    public void doInTabOne(File file) {
        if (file != null && file.isFile()) {
            if (!file.getName().endsWith(".xml")) {
                tempFile = file;
                tab1url.setText(file.getAbsolutePath());
                String text = DecryptTool.decryptToString(file.getAbsoluteFile());
                tab1TextArea.setText(text);
                tab1SaveBtn.setEnabled(true);
            } else {
                tab1TextArea.setText("配置文件请使用插件配置文件查看。");
            }
        }
    }

    class DefaultTransferHandler extends TransferHandler implements
            UIResource {
        private String tabName;

        public DefaultTransferHandler(String tab) {
            tabName = tab;
        }

        public void exportToClipboard(JComponent comp, Clipboard clipboard,
                                      int action) throws IllegalStateException {
            if (comp instanceof JTextComponent) {
                JTextComponent text = (JTextComponent) comp;
                int p0 = text.getSelectionStart();
                int p1 = text.getSelectionEnd();
                if (p0 != p1) {
                    try {
                        Document doc = text.getDocument();
                        String srcData = doc.getText(p0, p1 - p0);
                        StringSelection contents = new StringSelection(srcData);

                        // this may throw an IllegalStateException,
                        // but it will be caught and handled in the
                        // action that invoked this method
                        clipboard.setContents(contents, null);

                        if (action == TransferHandler.MOVE) {
                            doc.remove(p0, p1 - p0);
                        }
                    } catch (BadLocationException ble) {
                    }
                }
            }
        }

        public boolean importData(JComponent comp, Transferable t) {
            try {
                if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    List<File> fileList = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    File file = fileList.get(0);
                    if (ComparatorUtils.equals(tabName, TAB1)) {
                        doInTabOne(file);
                    } else {
                        doInTabTwo(file);
                    }
                    return true;
                }
            } catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }

            DataFlavor flavor = getFlavor(t.getTransferDataFlavors());

            if (flavor != null) {
                InputContext ic = comp.getInputContext();
                if (ic != null) {
                    ic.endComposition();
                }
                try {
                    String data = (String) t.getTransferData(flavor);

                    ((JTextComponent) comp).replaceSelection(data);
                    return true;
                } catch (UnsupportedFlavorException ufe) {
                } catch (IOException ioe) {
                }
            }
            return false;
        }

        public boolean canImport(JComponent comp,
                                 DataFlavor[] transferFlavors) {
//            JTextComponent c = (JTextComponent) comp;
//            if (!(c.isEditable() && c.isEnabled())) {
//                return false;
//            }
//            return (getFlavor(transferFlavors) != null);
            return true;
        }

        public int getSourceActions(JComponent c) {
            return NONE;
        }

        private DataFlavor getFlavor(DataFlavor[] flavors) {
            if (flavors != null) {
                for (DataFlavor flavor : flavors) {
                    if (flavor.equals(DataFlavor.stringFlavor)) {
                        return flavor;
                    }
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        new tool().init();
    }

    public void init() {
        JFrame frame = new JFrame("工具");
        frame.setContentPane(new tool().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setVisible(true);
    }
}
