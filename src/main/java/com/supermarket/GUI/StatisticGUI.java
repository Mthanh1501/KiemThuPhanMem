package com.supermarket.GUI;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.supermarket.BLL.ProductBLL;
import com.supermarket.BLL.StaffBLL;
import com.supermarket.DAL.MySQL;
import com.supermarket.DTO.Product;
import com.supermarket.DTO.Staff;
import com.supermarket.GUI.components.RoundedPanel;
import com.supermarket.GUI.components.chart.Chart;
import com.supermarket.GUI.components.chart.ModelChart;
import com.supermarket.GUI.components.chart.PanelBorderRadius;
import com.supermarket.GUI.components.chart.PanelShadow;
import javafx.scene.chart.XYChart;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticGUI extends RoundedPanel {

    public StatisticGUI() {
        super();
//        PlatformImpl.startup(() -> {});
        init();
//        selectedTab(0);
    }
//
    private RoundedPanel statisticMonth;
    private RoundedPanel statisticDay;
    private RoundedPanel statisticYear;
    private Chart chart;
    private RoundedPanel layoutChartAndData;
    private RoundedPanel statistic;
    private void init() {
        statistic = new RoundedPanel();
        layoutChartAndData = new RoundedPanel();
        statistic = new RoundedPanel();
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();
        labelText = new JLabel[3];
        this.add(tabbedPane);
        UIManager.put("TabbedPane.selectedBackground", Color.white);
        UIManager.put("TabbedPane.tabAreaInsets", new Insets(0, 0, 0, 0));
        UIManager.put("TabbedPane.tabInsets", new Insets(20, 20, 20, 20));
        UIManager.put("TabbedPane.selected", Color.RED);
        UIManager.put("TabbedPane.contentAreaColor", Color.GRAY);

        UIManager.put("Button.textIconGap", 10);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
        UIManager.put("PasswordField.showRevealButton", true);
        UIManager.put("ProgressBar.cycleTime", 6000);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("TextComponent.arc", 5);
        tabbedPane.setOpaque(false);
        Statistic();

    }


    private JLabel[] labelIcon;
    private JLabel[] labelText;
    private JLabel[] labelNumber;

    private PanelBorderRadius pnlChart;

    private PanelShadow panelShadow;

    private JTabbedPane tabbedPaneGeneral;
    private JTabbedPane tabbedPane;

    private JTextField[] textField;
    private JButton btnStatistic;
    private JButton btnRefresh;
    public void Statistic() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0,0,0,0);
        tabbedPane.setOpaque(false);
        statisticYear = new RoundedPanel();
        statisticMonth = new RoundedPanel();
        statisticDay = new RoundedPanel();
        tabbedPane.add("Tổng quát", statistic);
        tabbedPane.addTab("Thống kê theo năm", statisticYear);
        tabbedPane.addTab("Thống kê theo quý", statisticMonth);
        tabbedPane.addTab("Thống kê theo tháng", statisticDay);

        statistic.setLayout(new BorderLayout());
        statistic.add(genneral());
        tabbedPane.addChangeListener(new ChangeListener() {
            int currentMonth, currentYear, currentQuarter;
            List<Double> expenses, amount;

            @Override
            public void stateChanged(ChangeEvent e) {
                switch (tabbedPane.getSelectedIndex()) {
                    case 1 -> {
                        addChart(statisticYear);
                        expenses = new ArrayList<>();
                        amount = new ArrayList<>();
                        labelText[0].setText("THỐNG KÊ THEO 3 NĂM GẦN NHẤT ");
                        statisticYear.add(labelText[0], BorderLayout.NORTH);
                        chart.clear();
                        int currentYear = Year.now().getValue();
                        for (int i = currentYear - 2; i <= currentYear; i ++) {
                            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
                            series1.setName("Năm " + i);
                            try {
                                List<List<String>> inventory = MySQL.executeQueryStatistic("SELECT SUM(`import`.total) " +
                                    "FROM `import` " +
                                    "WHERE YEAR(`import`.received_date) = " + i);
                                expenses.add(Double.parseDouble(inventory.get(0).get(0).split("\\.")[0]));

                            } catch (SQLException | IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            try {
                                List<List<String>> inventory = MySQL.executeQueryStatistic("SELECT SUM(`receipt`.total) " +
                                    "FROM `receipt` " +
                                    "WHERE YEAR(`receipt`.invoice_date) = " + i);
                                amount.add(Double.parseDouble(inventory.get(0).get(0).split("\\.")[0]));

                            } catch (SQLException | IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            chart.addData(new ModelChart(String.valueOf(i), new double[]{expenses.get(expenses.size() - 1), amount.get(expenses.size() - 1), amount.get(expenses.size() - 1) - expenses.get(expenses.size() - 1)}));
                        }
                        chart.start();
                    }
                    case 2 -> {
                        addChart(statisticMonth);
                        currentMonth = YearMonth.now().getMonthValue();
                        currentYear = Year.now().getValue();
                        currentQuarter = (currentMonth - 1) / 3 + 1;
                        expenses = new ArrayList<>();
                        amount = new ArrayList<>();
                        labelText[1].setText("THỐNG KÊ THEO QUÝ TRONG NĂM " + currentYear);
                        statisticMonth.add(labelText[1], BorderLayout.NORTH);
                        for (int i = 1; i <= currentQuarter; i++) {
                            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
                            series1.setName("Quý " + i);
                            try {
                                List<List<String>> inventory = MySQL.executeQueryStatistic("SELECT SUM(`import`.total) " +
                                    "FROM `import` " +
                                    "WHERE YEAR(`import`.received_date) = " + currentYear + " AND QUARTER(`import`.received_date) = " + i);
                                expenses.add(Double.parseDouble(inventory.get(0).get(0).split("\\.")[0]));


                            } catch (SQLException | IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            try {
                                List<List<String>> inventory = MySQL.executeQueryStatistic("SELECT SUM(`receipt`.total) " +
                                    "FROM `receipt` " +
                                    "WHERE YEAR(`receipt`.invoice_date) = " + currentYear + " AND QUARTER(`receipt`.invoice_date) = " + i);
                                amount.add(Double.parseDouble(inventory.get(0).get(0).split("\\.")[0]));

                            } catch (SQLException | IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        chart.clear();
                        chart.addData(new ModelChart("Quý 1", new double[]{expenses.get(0), amount.get(0), amount.get(0) - expenses.get(0)}));
                        chart.addData(new ModelChart("Quý 2", new double[]{expenses.get(1), amount.get(1), amount.get(1) - expenses.get(1)}));
                        chart.addData(new ModelChart("Quý 3", new double[]{expenses.get(2), amount.get(2), amount.get(2) - expenses.get(2)}));
                        chart.addData(new ModelChart("Quý 4", new double[]{expenses.get(3), amount.get(3), amount.get(3) - expenses.get(3)}));
                        chart.start();

                    }
                    case 3 -> {
                        addChart(statisticDay);
                        currentMonth = YearMonth.now().getMonthValue();
                        currentYear = Year.now().getValue();
                        expenses = new ArrayList<>();
                        amount = new ArrayList<>();
                        labelText[2].setText("THỐNG KÊ THEO THÁNG TRONG NĂM " + currentYear);
                        statisticDay.add(labelText[2], BorderLayout.NORTH);
                        for (int i = 1; i <= currentMonth; i++) {
                            try {
                                List<List<String>> inventory = MySQL.executeQueryStatistic("SELECT SUM(`import`.total) " +
                                    "FROM `import` " +
                                    "WHERE YEAR(`import`.received_date) = " + currentYear + " AND MONTH(`import`.received_date) = " + i);
                                expenses.add(Double.parseDouble(inventory.get(0).get(0).split("\\.")[0]));

                            } catch (SQLException | IOException ex) {
                                throw new RuntimeException(ex);
                            }

                            try {
                                List<List<String>> inventory = MySQL.executeQueryStatistic("SELECT SUM(`receipt`.total) " +
                                    "FROM `receipt` " +
                                    "WHERE YEAR(`receipt`.invoice_date) = " + currentYear + " AND MONTH(`receipt`.invoice_date) = " + i);
                                amount.add(Double.parseDouble(inventory.get(0).get(0).split("\\.")[0]));

                            } catch (SQLException | IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        chart.clear();
                        chart.addData(new ModelChart("Tháng 1", new double[]{expenses.get(0), amount.get(0), amount.get(0) - expenses.get(0)}));
                        chart.addData(new ModelChart("Tháng 2", new double[]{expenses.get(1), amount.get(1), amount.get(1) - expenses.get(1)}));
                        chart.addData(new ModelChart("Tháng 3", new double[]{expenses.get(2), amount.get(2), amount.get(2) - expenses.get(2)}));
                        chart.addData(new ModelChart("Tháng 4", new double[]{expenses.get(3), amount.get(3), amount.get(3) - expenses.get(3)}));
                        chart.addData(new ModelChart("Tháng 5", new double[]{expenses.get(4), amount.get(4), amount.get(4) - expenses.get(4)}));
                        chart.addData(new ModelChart("Tháng 6", new double[]{expenses.get(5), amount.get(5), amount.get(5) - expenses.get(5)}));
                        chart.addData(new ModelChart("Tháng 7", new double[]{expenses.get(6), amount.get(6), amount.get(6) - expenses.get(6)}));
                        chart.addData(new ModelChart("Tháng 8", new double[]{expenses.get(7), amount.get(7), amount.get(7) - expenses.get(7)}));
                        chart.addData(new ModelChart("Tháng 9", new double[]{expenses.get(8), amount.get(8), amount.get(8) - expenses.get(8)}));
                        chart.addData(new ModelChart("Tháng 10", new double[]{expenses.get(9), amount.get(9), amount.get(9) - expenses.get(9)}));
                        chart.addData(new ModelChart("Tháng 11", new double[]{expenses.get(10), amount.get(10), amount.get(10) - expenses.get(10)}));
                        chart.addData(new ModelChart("Tháng 12", new double[]{expenses.get(11), amount.get(11), amount.get(11) - expenses.get(11)}));
                        chart.start();
                    }
                }
            }
        });

        for (int i = 0; i < labelText.length; i++) {
            labelText[i] = new JLabel();
            labelText[i].setPreferredSize(new Dimension(1000,50));
            labelText[i].setFont(new Font("Times New Roman",Font.PLAIN,16));
            labelText[i].setHorizontalAlignment(SwingConstants.CENTER);
        }

        statisticYear.add(labelText[0], BorderLayout.NORTH);
        statisticMonth.add(labelText[1], BorderLayout.NORTH);
        statisticDay.add(labelText[2], BorderLayout.NORTH);

        layoutChartAndData = new RoundedPanel();
        chart = new Chart();
        chart.addLegend("Vốn", Color.decode("#7b4397"));
        chart.addLegend("Doanh thu", Color.decode("#e65c00"));
        chart.addLegend("Lợi nhuận", Color.decode("#0099F7"));
        layoutChartAndData.setBackground(null);
        layoutChartAndData.setLayout(new BorderLayout());
        layoutChartAndData.add(chart, BorderLayout.CENTER);
    }

    public void addChart(RoundedPanel statistic) {
        statistic.removeAll();
        statistic.setLayout(new BorderLayout());
        statistic.add(layoutChartAndData, BorderLayout.CENTER);
        statistic.repaint();
        statistic.revalidate();
    }
    private RoundedPanel genneral() {
        ProductBLL productBLL = new ProductBLL();
        StaffBLL staffBLL = new StaffBLL();
        RoundedPanel genneralTab = new RoundedPanel();
        List<RoundedPanel> listItem = new ArrayList<>();
        List<JLabel> listIcon = new ArrayList<>();
        List<JLabel> listValue = new ArrayList<>();
        List<JLabel> listTitle = new ArrayList<>();
        genneralTab.setLayout(new MigLayout("", "10[]15[]10", "10[]20[]"));
        genneralTab.setPreferredSize(new Dimension(1140, 760));
        genneralTab.setBackground(new Color(0xFFBDD2DB, true));

        for (int i = 0; i < 12; i++) {
            RoundedPanel roundedPanel = new RoundedPanel();
            roundedPanel.setLayout(new MigLayout());
            roundedPanel.setPreferredSize(new Dimension(274, 225));
            roundedPanel.setBackground(new Color(0x80B27F7F, true));
            listItem.add(roundedPanel);

            JLabel label = new JLabel();
            listIcon.add(label);

            JLabel label1 = new JLabel();
            label1.setFont(new Font("Public Sans", Font.BOLD, 40));
            label1.setForeground(new Color(0x2C2E31));
            listValue.add(label1);

            JLabel label2 = new JLabel();
            label2.setFont(new Font("Public Sans", Font.PLAIN, 15));
            listTitle.add(label2);
        }

//        listIcon.get(0).setIcon(new FlatSVGIcon("icon/customerr.svg"));
//
//        try {
//            List<List<String>> numberOfCustomer = MySQL.executeQueryStatistic("SELECT COUNT(customer.id) FROM customer WHERE customer.membership = 1 and customer.deleted = 0");
//            listValue.get(0).setText(numberOfCustomer.get(0).get(0));
//        } catch (SQLException | IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        listTitle.get(0).setText("<html>Số lượng khách hàng thành viên hiện tại<html>");
//
//        listItem.get(0).add(listIcon.get(0));
//        listItem.get(0).add(listValue.get(0),"wrap");
//        listItem.get(0).add(listTitle.get(0),"span 2 1");
//
//        genneralTab.add(listItem.get(0));

        listIcon.get(1).setIcon(new FlatSVGIcon("icon/stafff.svg"));

        try {
            List<List<String>> numberOfStaff = MySQL.executeQueryStatistic("SELECT COUNT(staff.id) FROM staff WHERE staff.deleted = 0");
            listValue.get(1).setText(numberOfStaff.get(0).get(0));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        listTitle.get(1).setText("<html>Số lượng nhân viên hiện tại<html>");

        listItem.get(1).add(listIcon.get(1));
        listItem.get(1).add(listValue.get(1), "wrap");
        listItem.get(1).add(listTitle.get(1), "span 2 1");

        genneralTab.add(listItem.get(1));


        try {
            List<List<String>> bestSeller = MySQL.executeQueryStatistic("SELECT product.id, product.image, SUM(receipt_detail.quantity) " +
                "FROM receipt_detail JOIN product on receipt_detail.product_id = product.id " +
                "JOIN receipt on receipt.id = receipt_detail.receipt_id " +
                "WHERE (MONTH(CURDATE()) = MONTH(`receipt`.invoice_date)) AND (YEAR(CURDATE()) = YEAR(`receipt`.invoice_date)) " +
                "GROUP BY product.id, product.image " +
                "ORDER BY SUM(receipt_detail.quantity) DESC" +
                " LIMIT 1");
            if (!bestSeller.isEmpty()) {
                listIcon.get(2).setIcon(new FlatSVGIcon(bestSeller.get(0).get(1)));
                listValue.get(2).setText(bestSeller.get(0).get(2));
                Product product = productBLL.findProductsBy(Map.of("id", Integer.parseInt(bestSeller.get(0).get(0)))).get(0);
                listTitle.get(2).setText("<html>Sản phẩm bán chạy nhất tháng: <br>" + product.getName() + "<html>");
            } else {
                listValue.get(2).setText("0");
                listTitle.get(2).setText("<html>Sản phẩm bán chạy nhất tháng:<html>");
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }


        listItem.get(2).add(listIcon.get(2));
        listItem.get(2).add(listValue.get(2), "wrap");
        listItem.get(2).add(listTitle.get(2), "span 2 1");

        genneralTab.add(listItem.get(2));

        try {
            List<List<String>> bestSeller = MySQL.executeQueryStatistic("SELECT product.id, product.image, SUM(receipt_detail.quantity) " +
                "FROM receipt_detail JOIN product on receipt_detail.product_id = product.id " +
                "JOIN receipt on receipt.id = receipt_detail.receipt_id " +
                "WHERE (MONTH(CURDATE()) = MONTH(`receipt`.invoice_date)) AND (YEAR(CURDATE()) = YEAR(`receipt`.invoice_date)) " +
                "GROUP BY product.id, product.image " +
                "ORDER BY SUM(receipt_detail.quantity) ASC" +
                " LIMIT 1");
            if (!bestSeller.isEmpty()) {
                listIcon.get(3).setIcon(new FlatSVGIcon(bestSeller.get(0).get(1)));
                listValue.get(3).setText(bestSeller.get(0).get(2));
                Product product = productBLL.findProductsBy(Map.of("id", Integer.parseInt(bestSeller.get(0).get(0)))).get(0);
                listTitle.get(3).setText("<html>Sản phẩm bán ít nhất tháng: <br>" + product.getName() + "<html>");
            } else {
                listValue.get(3).setText("0");
                listTitle.get(3).setText("<html>Sản phẩm bán ít nhất tháng:<html>");
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        listItem.get(3).add(listIcon.get(3));
        listItem.get(3).add(listValue.get(3), "wrap");
        listItem.get(3).add(listTitle.get(3), "span 2 1");

        genneralTab.add(listItem.get(3), "wrap");

        try {
            List<List<String>> bestStaff = MySQL.executeQueryStatistic("SELECT staff.id, staff.name, COUNT(receipt.id) " +
                "FROM receipt JOIN staff on receipt.staff_id = staff.id " +
                "WHERE (MONTH(CURDATE()) = MONTH(`receipt`.invoice_date)) AND (YEAR(CURDATE()) = YEAR(`receipt`.invoice_date)) " +
                "GROUP BY staff.id, staff.name " +
                "ORDER BY COUNT(receipt.id) DESC" +
                " LIMIT 1");
            listIcon.get(4).setIcon(new FlatSVGIcon("icon/stafff.svg"));

            if (!bestStaff.isEmpty()) {
                listValue.get(4).setText(bestStaff.get(0).get(2));
                Staff staff = staffBLL.findStaffsBy(Map.of("id", Integer.parseInt(bestStaff.get(0).get(0)))).get(0);
                listTitle.get(4).setText("<html>Nhân viên của tháng: <br>" + staff.getName() + "<html>");
            } else {
                listValue.get(4).setText("0");
                listTitle.get(4).setText("<html>Nhân viên của tháng:<html>");
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        listItem.get(4).add(listIcon.get(4));
        listItem.get(4).add(listValue.get(4), "wrap");
        listItem.get(4).add(listTitle.get(4), "span 2 1");

        genneralTab.add(listItem.get(4));

        try {
            List<List<String>> mostInventory = MySQL.executeQueryStatistic("SELECT product.id, product.image, shipment.remain " +
                "FROM shipment JOIN product on shipment.product_id = product.id " +
                "ORDER BY shipment.remain ASC" +
                " LIMIT 1");
            if (!mostInventory.isEmpty()) {
                listIcon.get(5).setIcon(new FlatSVGIcon(mostInventory.get(0).get(1)));
                listValue.get(5).setText(mostInventory.get(0).get(2));
                Product product = productBLL.findProductsBy(Map.of("id", Integer.parseInt(mostInventory.get(0).get(0)))).get(0);
                listTitle.get(5).setText("<html>Sản phẩm tồn kho nhiều nhất: <br>" + product.getName() + "<html>");
            } else {
                listValue.get(5).setText("0");
                listTitle.get(5).setText("<html>Sản phẩm tồn kho nhiều nhất:<html>");
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        listItem.get(5).add(listIcon.get(5));
        listItem.get(5).add(listValue.get(5), "wrap");
        listItem.get(5).add(listTitle.get(5), "span 2 1");

        genneralTab.add(listItem.get(5));

        try {
            List<List<String>> mostInventory = MySQL.executeQueryStatistic("SELECT SUM(`import`.total) " +
                "FROM `import` " +
                "WHERE (MONTH(CURDATE()) - MONTH(`import`.received_date)) = 0 AND (YEAR(CURDATE()) - YEAR(`import`.received_date)) = 0");
            listIcon.get(6).setIcon(new FlatSVGIcon("icon/importGoods.svg"));
            listValue.get(6).setText("<html>" + mostInventory.get(0).get(0) + "<br>vnđ<html>");
            listTitle.get(6).setText("<html>Chi phí nhập kho của tháng<html>");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        listItem.get(6).add(listIcon.get(6), "wrap");
        listItem.get(6).add(listValue.get(6), "wrap");
        listItem.get(6).add(listTitle.get(6), "wrap");

        genneralTab.add(listItem.get(6));

        try {
            List<List<String>> mostInventory = MySQL.executeQueryStatistic("SELECT SUM(`receipt`.total) " +
                "FROM `receipt` " +
                "WHERE (MONTH(CURDATE()) - MONTH(`receipt`.invoice_date)) = 0 AND (YEAR(CURDATE()) - YEAR(`receipt`.invoice_date)) = 0");
            listIcon.get(7).setIcon(new FlatSVGIcon("icon/imcome.svg"));
            listValue.get(7).setText("<html>" + mostInventory.get(0).get(0) + "<br>vnđ<html>");
            listTitle.get(7).setText("<html>Doanh thu của tháng<html>");
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        listItem.get(7).add(listIcon.get(7), "wrap");
        listItem.get(7).add(listValue.get(7), "wrap");
        listItem.get(7).add(listTitle.get(7), "wrap");

        genneralTab.add(listItem.get(7), "wrap");
        return genneralTab;
    }

}
