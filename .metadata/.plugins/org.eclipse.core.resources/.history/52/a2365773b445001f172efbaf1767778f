/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package services;

import java.awt.BorderLayout;
import java.awt.Container;

import java.util.Map;
import org.apache.log4j.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import application.Main;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;


public class ReportView extends JFrame

{
	private final Logger LOG = Logger.getLogger(ReportView.class);
    public ReportView(JasperReport fileName, Map<String, Object> parameters)
    {
        super("Patient Report");
        TitledPane  jPanel1 = new TitledPane ();
        JPanel pan = new JPanel();
            try
            {
                JasperPrint print = JasperFillManager.fillReport(fileName, parameters,new JREmptyDataSource());

                JRViewer viewer = new JRViewer(print);
                viewer.setOpaque(true);
                viewer.setVisible(true);
                this.add(viewer);
                this.setSize(700, 500);
                this.setVisible(true);
//                System.out.println("Report Genarated Successfully");
//    			Main.getInstance().getUtility().showNotification("Information", "Success",
//    					"Report generated successfully");
                //ScrollPane disablePane = (ScrollPane) Main.getInstance().getParentScene().lookup("#previwScrollPane");

                //pan.add(viewer);
                //jPanel1.setContent((Node)pan);
                /*jPanel1.setLayout(new BorderLayout());
                jPanel1.repaint();
                jPanel1.add(viewer);
                jPanel1.revalidate();*/
               // disablePane.setContent(jPanel1);
                /*Container c = getContentPane();
                c.add(viewer);*/
            }
            catch (JRException j){
            	LOG.info("ERROR#########################################"+j.getMessage());
                j.printStackTrace();

            }
            setBounds(10, 10, 900, 700);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

}