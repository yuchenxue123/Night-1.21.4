package cute.neko.night.main;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;

public class Main extends JDialog {

    private Point position;

    public Main(Frame parent) {
        super(parent, "", true);

        setSize(400, 220);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        setUndecorated(true);
        getRootPane().setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                position = event.getLocationOnScreen();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent event) {
                Point current = event.getLocationOnScreen();
                int xMoved = current.x - position.x;
                int yMoved = current.y - position.y;

                setLocation(getX() + xMoved, getY() + yMoved);

                position = current;
            }
        });

        JLabel label = new JLabel(
                "<html><body style='width: 350px; padding: 10px;'>"
                        + "<b style='font-size: 15px;'>你也是神人了，双击打开这个文件</b><br><br>"
                        + "这是一个 1.21.4 Fabric Mod<br><br>"
                        + "请将其放入 .minecraft/mods 文件夹中"
                        + "</body></html>"
        );
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(Color.DARK_GRAY);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);

        JButton okButton = new JButton("<html><b><font color='#ffffff'>我知道了</font></b></html>");
        okButton.setFocusPainted(false);
        okButton.setBackground(new Color(72, 133, 237));
        okButton.setForeground(Color.WHITE);
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        okButton.setPreferredSize(new Dimension(120, 36));
        okButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        okButton.setUI(new BasicButtonUI() {
            @Override
            public void installUI(JComponent c) {
                super.installUI(c);
                c.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
            }
        });

        okButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                okButton.setBackground(new Color(66, 121, 214));
            }

            public void mouseExited(MouseEvent evt) {
                okButton.setBackground(new Color(72, 133, 237));
            }
        });

        okButton.addActionListener(event -> {
            dispose();
            System.exit(0);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(okButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main dialog = new Main(null);
            dialog.setVisible(true);
        });
    }
}
