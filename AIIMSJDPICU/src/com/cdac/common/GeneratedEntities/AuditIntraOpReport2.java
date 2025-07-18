/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.GeneratedEntities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.math.BigInteger;


/**
* The persistent class for the auditintraopreport2 database table.
* 
 */
@Entity
@Table(name="auditintraopreport2")
public class AuditIntraOpReport2 implements Serializable {
                private static final long serialVersionUID = 1L;

                @Id
                @GeneratedValue(strategy = IDENTITY)
                private Integer auditReportID;

                @Column(name="`ABG-1-BB`")
            	private String abg_1_bb;

            	@Column(name="`ABG-1-BE`")
            	private String abg_1_be;

            	@Column(name="`ABG-1-CA2`")
            	private String abg_1_ca2;

            	@Column(name="`ABG-1-CL`")
            	private String abg_1_cl;

            	@Column(name="`ABG-1-FIO2`")
            	private String abg_1_fio2;

            	@Column(name="`ABG-1-HCO3`")
            	private String abg_1_hco3;

            	@Column(name="`ABG-1-HCT`")
            	private String abg_1_hct;

            	@Column(name="`ABG-1-K`")
            	private String abg_1_k;

            	@Column(name="`ABG-1-LACTATE`")
            	private String abg_1_lactate;

            	@Column(name="`ABG-1-NA`")
            	private String abg_1_na;

            	@Column(name="`ABG-1-OTHERS`")
            	private String abg_1_others;

            	@Column(name="`ABG-1-PCO2`")
            	private String abg_1_pco2;

            	@Column(name="`ABG-1-PH`")
            	private String abg_1_ph;

            	@Column(name="`ABG-1-PO2`")
            	private String abg_1_po2;

            	@Column(name="`ABG-1-SPO2`")
            	private String abg_1_spo2;

            	@Column(name="`ABG-1-TIME`")
            	private String abg_1_time;

            	@Column(name="`ABG-10-BB`")
            	private String abg_10_bb;

            	@Column(name="`ABG-10-BE`")
            	private String abg_10_be;

            	@Column(name="`ABG-10-CA2`")
            	private String abg_10_ca2;

            	@Column(name="`ABG-10-CL`")
            	private String abg_10_cl;

            	@Column(name="`ABG-10-FIO2`")
            	private String abg_10_fio2;

            	@Column(name="`ABG-10-HCO3`")
            	private String abg_10_hco3;

            	@Column(name="`ABG-10-HCT`")
            	private String abg_10_hct;

            	@Column(name="`ABG-10-K`")
            	private String abg_10_k;

            	@Column(name="`ABG-10-LACTATE`")
            	private String abg_10_lactate;

            	@Column(name="`ABG-10-NA`")
            	private String abg_10_na;

            	@Column(name="`ABG-10-OTHERS`")
            	private String abg_10_others;

            	@Column(name="`ABG-10-PCO2`")
            	private String abg_10_pco2;

            	@Column(name="`ABG-10-PH`")
            	private String abg_10_ph;

            	@Column(name="`ABG-10-PO2`")
            	private String abg_10_po2;

            	@Column(name="`ABG-10-SPO2`")
            	private String abg_10_spo2;

            	@Column(name="`ABG-10-TIME`")
            	private String abg_10_time;

            	@Column(name="`ABG-2-BB`")
            	private String abg_2_bb;

            	@Column(name="`ABG-2-BE`")
            	private String abg_2_be;

            	@Column(name="`ABG-2-CA2`")
            	private String abg_2_ca2;

            	@Column(name="`ABG-2-CL`")
            	private String abg_2_cl;

            	@Column(name="`ABG-2-FIO2`")
            	private String abg_2_fio2;

            	@Column(name="`ABG-2-HCO3`")
            	private String abg_2_hco3;

            	@Column(name="`ABG-2-HCT`")
            	private String abg_2_hct;

            	@Column(name="`ABG-2-K`")
            	private String abg_2_k;

            	@Column(name="`ABG-2-LACTATE`")
            	private String abg_2_lactate;

            	@Column(name="`ABG-2-NA`")
            	private String abg_2_na;

            	@Column(name="`ABG-2-OTHERS`")
            	private String abg_2_others;

            	@Column(name="`ABG-2-PCO2`")
            	private String abg_2_pco2;

            	@Column(name="`ABG-2-PH`")
            	private String abg_2_ph;

            	@Column(name="`ABG-2-PO2`")
            	private String abg_2_po2;

            	@Column(name="`ABG-2-SPO2`")
            	private String abg_2_spo2;

            	@Column(name="`ABG-2-TIME`")
            	private String abg_2_time;

            	@Column(name="`ABG-3-BB`")
            	private String abg_3_bb;

            	@Column(name="`ABG-3-BE`")
            	private String abg_3_be;

            	@Column(name="`ABG-3-CA2`")
            	private String abg_3_ca2;

            	@Column(name="`ABG-3-CL`")
            	private String abg_3_cl;

            	@Column(name="`ABG-3-FIO2`")
            	private String abg_3_fio2;

            	@Column(name="`ABG-3-HCO3`")
            	private String abg_3_hco3;

            	@Column(name="`ABG-3-HCT`")
            	private String abg_3_hct;

            	@Column(name="`ABG-3-K`")
            	private String abg_3_k;

            	@Column(name="`ABG-3-LACTATE`")
            	private String abg_3_lactate;

            	@Column(name="`ABG-3-NA`")
            	private String abg_3_na;

            	@Column(name="`ABG-3-OTHERS`")
            	private String abg_3_others;

            	@Column(name="`ABG-3-PCO2`")
            	private String abg_3_pco2;

            	@Column(name="`ABG-3-PH`")
            	private String abg_3_ph;

            	@Column(name="`ABG-3-PO2`")
            	private String abg_3_po2;

            	@Column(name="`ABG-3-SPO2`")
            	private String abg_3_spo2;

            	@Column(name="`ABG-3-TIME`")
            	private String abg_3_time;

            	@Column(name="`ABG-4-BB`")
            	private String abg_4_bb;

            	@Column(name="`ABG-4-BE`")
            	private String abg_4_be;

            	@Column(name="`ABG-4-CA2`")
            	private String abg_4_ca2;

            	@Column(name="`ABG-4-CL`")
            	private String abg_4_cl;

            	@Column(name="`ABG-4-FIO2`")
            	private String abg_4_fio2;

            	@Column(name="`ABG-4-HCO3`")
            	private String abg_4_hco3;

            	@Column(name="`ABG-4-HCT`")
            	private String abg_4_hct;

            	@Column(name="`ABG-4-K`")
            	private String abg_4_k;

            	@Column(name="`ABG-4-LACTATE`")
            	private String abg_4_lactate;

            	@Column(name="`ABG-4-NA`")
            	private String abg_4_na;

            	@Column(name="`ABG-4-OTHERS`")
            	private String abg_4_others;

            	@Column(name="`ABG-4-PCO2`")
            	private String abg_4_pco2;

            	@Column(name="`ABG-4-PH`")
            	private String abg_4_ph;

            	@Column(name="`ABG-4-PO2`")
            	private String abg_4_po2;

            	@Column(name="`ABG-4-SPO2`")
            	private String abg_4_spo2;

            	@Column(name="`ABG-4-TIME`")
            	private String abg_4_time;

            	@Column(name="`ABG-5-BB`")
            	private String abg_5_bb;

            	@Column(name="`ABG-5-BE`")
            	private String abg_5_be;

            	@Column(name="`ABG-5-CA2`")
            	private String abg_5_ca2;

            	@Column(name="`ABG-5-CL`")
            	private String abg_5_cl;

            	@Column(name="`ABG-5-FIO2`")
            	private String abg_5_fio2;

            	@Column(name="`ABG-5-HCO3`")
            	private String abg_5_hco3;

            	@Column(name="`ABG-5-HCT`")
            	private String abg_5_hct;

            	@Column(name="`ABG-5-K`")
            	private String abg_5_k;

            	@Column(name="`ABG-5-LACTATE`")
            	private String abg_5_lactate;

            	@Column(name="`ABG-5-NA`")
            	private String abg_5_na;

            	@Column(name="`ABG-5-OTHERS`")
            	private String abg_5_others;

            	@Column(name="`ABG-5-PCO2`")
            	private String abg_5_pco2;

            	@Column(name="`ABG-5-PH`")
            	private String abg_5_ph;

            	@Column(name="`ABG-5-PO2`")
            	private String abg_5_po2;

            	@Column(name="`ABG-5-SPO2`")
            	private String abg_5_spo2;

            	@Column(name="`ABG-5-TIME`")
            	private String abg_5_time;

            	@Column(name="`ABG-6-BB`")
            	private String abg_6_bb;

            	@Column(name="`ABG-6-BE`")
            	private String abg_6_be;

            	@Column(name="`ABG-6-CA2`")
            	private String abg_6_ca2;

            	@Column(name="`ABG-6-CL`")
            	private String abg_6_cl;

            	@Column(name="`ABG-6-FIO2`")
            	private String abg_6_fio2;

            	@Column(name="`ABG-6-HCO3`")
            	private String abg_6_hco3;

            	@Column(name="`ABG-6-HCT`")
            	private String abg_6_hct;

            	@Column(name="`ABG-6-K`")
            	private String abg_6_k;

            	@Column(name="`ABG-6-LACTATE`")
            	private String abg_6_lactate;

            	@Column(name="`ABG-6-NA`")
            	private String abg_6_na;

            	@Column(name="`ABG-6-OTHERS`")
            	private String abg_6_others;

            	@Column(name="`ABG-6-PCO2`")
            	private String abg_6_pco2;

            	@Column(name="`ABG-6-PH`")
            	private String abg_6_ph;

            	@Column(name="`ABG-6-PO2`")
            	private String abg_6_po2;

            	@Column(name="`ABG-6-SPO2`")
            	private String abg_6_spo2;

            	@Column(name="`ABG-6-TIME`")
            	private String abg_6_time;

            	@Column(name="`ABG-7-BB`")
            	private String abg_7_bb;

            	@Column(name="`ABG-7-BE`")
            	private String abg_7_be;

            	@Column(name="`ABG-7-CA2`")
            	private String abg_7_ca2;

            	@Column(name="`ABG-7-CL`")
            	private String abg_7_cl;

            	@Column(name="`ABG-7-FIO2`")
            	private String abg_7_fio2;

            	@Column(name="`ABG-7-HCO3`")
            	private String abg_7_hco3;

            	@Column(name="`ABG-7-HCT`")
            	private String abg_7_hct;

            	@Column(name="`ABG-7-K`")
            	private String abg_7_k;

            	@Column(name="`ABG-7-LACTATE`")
            	private String abg_7_lactate;

            	@Column(name="`ABG-7-NA`")
            	private String abg_7_na;

            	@Column(name="`ABG-7-OTHERS`")
            	private String abg_7_others;

            	@Column(name="`ABG-7-PCO2`")
            	private String abg_7_pco2;

            	@Column(name="`ABG-7-PH`")
            	private String abg_7_ph;

            	@Column(name="`ABG-7-PO2`")
            	private String abg_7_po2;

            	@Column(name="`ABG-7-SPO2`")
            	private String abg_7_spo2;

            	@Column(name="`ABG-7-TIME`")
            	private String abg_7_time;

            	@Column(name="`ABG-8-BB`")
            	private String abg_8_bb;

            	@Column(name="`ABG-8-BE`")
            	private String abg_8_be;

            	@Column(name="`ABG-8-CA2`")
            	private String abg_8_ca2;

            	@Column(name="`ABG-8-CL`")
            	private String abg_8_cl;

            	@Column(name="`ABG-8-FIO2`")
            	private String abg_8_fio2;

            	@Column(name="`ABG-8-HCO3`")
            	private String abg_8_hco3;

            	@Column(name="`ABG-8-HCT`")
            	private String abg_8_hct;

            	@Column(name="`ABG-8-K`")
            	private String abg_8_k;

            	@Column(name="`ABG-8-LACTATE`")
            	private String abg_8_lactate;

            	@Column(name="`ABG-8-NA`")
            	private String abg_8_na;

            	@Column(name="`ABG-8-OTHERS`")
            	private String abg_8_others;

            	@Column(name="`ABG-8-PCO2`")
            	private String abg_8_pco2;

            	@Column(name="`ABG-8-PH`")
            	private String abg_8_ph;

            	@Column(name="`ABG-8-PO2`")
            	private String abg_8_po2;

            	@Column(name="`ABG-8-SPO2`")
            	private String abg_8_spo2;

            	@Column(name="`ABG-8-TIME`")
            	private String abg_8_time;

            	@Column(name="`ABG-9-BB`")
            	private String abg_9_bb;

            	@Column(name="`ABG-9-BE`")
            	private String abg_9_be;

            	@Column(name="`ABG-9-CA2`")
            	private String abg_9_ca2;

            	@Column(name="`ABG-9-CL`")
            	private String abg_9_cl;

            	@Column(name="`ABG-9-FIO2`")
            	private String abg_9_fio2;

            	@Column(name="`ABG-9-HCO3`")
            	private String abg_9_hco3;

            	@Column(name="`ABG-9-HCT`")
            	private String abg_9_hct;

            	@Column(name="`ABG-9-K`")
            	private String abg_9_k;

            	@Column(name="`ABG-9-LACTATE`")
            	private String abg_9_lactate;

            	@Column(name="`ABG-9-NA`")
            	private String abg_9_na;

            	@Column(name="`ABG-9-OTHERS`")
            	private String abg_9_others;

            	@Column(name="`ABG-9-PCO2`")
            	private String abg_9_pco2;

            	@Column(name="`ABG-9-PH`")
            	private String abg_9_ph;

            	@Column(name="`ABG-9-PO2`")
            	private String abg_9_po2;

            	@Column(name="`ABG-9-SPO2`")
            	private String abg_9_spo2;

            	@Column(name="`ABG-9-TIME`")
            	private String abg_9_time;

            	@Column(name="`ACT-1-TIME`")
            	private String act_1_time;

            	@Column(name="`ACT-1-VALUE`")
            	private BigDecimal act_1_value;

            	@Column(name="`ACT-10-TIME`")
            	private String act_10_time;

            	@Column(name="`ACT-10-VALUE`")
            	private BigDecimal act_10_value;

            	@Column(name="`ACT-2-TIME`")
            	private String act_2_time;

            	@Column(name="`ACT-2-VALUE`")
            	private BigDecimal act_2_value;

            	@Column(name="`ACT-3-TIME`")
            	private String act_3_time;

            	@Column(name="`ACT-3-VALUE`")
            	private BigDecimal act_3_value;

            	@Column(name="`ACT-4-TIME`")
            	private String act_4_time;

            	@Column(name="`ACT-4-VALUE`")
            	private BigDecimal act_4_value;

            	@Column(name="`ACT-5-TIME`")
            	private String act_5_time;

            	@Column(name="`ACT-5-VALUE`")
            	private BigDecimal act_5_value;

            	@Column(name="`ACT-6-TIME`")
            	private String act_6_time;

            	@Column(name="`ACT-6-VALUE`")
            	private BigDecimal act_6_value;

            	@Column(name="`ACT-7-TIME`")
            	private String act_7_time;

            	@Column(name="`ACT-7-VALUE`")
            	private BigDecimal act_7_value;

            	@Column(name="`ACT-8-TIME`")
            	private String act_8_time;

            	@Column(name="`ACT-8-VALUE`")
            	private BigDecimal act_8_value;

            	@Column(name="`ACT-9-TIME`")
            	private String act_9_time;

            	@Column(name="`ACT-9-VALUE`")
            	private BigDecimal act_9_value;

            	private Long caseID;

            	private String createdBy;

            	@Temporal(TemporalType.TIMESTAMP)
            	private Date createdTime;

            	private Float fluid_NS;

            	private Float fluid_Other;

            	private Float fluid_RL;

            	private String output_Blood;

            	private String output_Urine;

            	@Column(name="`SUGAR-1-TIME`")
            	private String sugar_1_time;

            	@Column(name="`SUGAR-1-VALUE`")
            	private BigDecimal sugar_1_value;

            	@Column(name="`SUGAR-10-TIME`")
            	private String sugar_10_time;

            	@Column(name="`SUGAR-10-VALUE`")
            	private BigDecimal sugar_10_value;

            	@Column(name="`SUGAR-2-TIME`")
            	private String sugar_2_time;

            	@Column(name="`SUGAR-2-VALUE`")
            	private BigDecimal sugar_2_value;

            	@Column(name="`SUGAR-3-TIME`")
            	private String sugar_3_time;

            	@Column(name="`SUGAR-3-VALUE`")
            	private BigDecimal sugar_3_value;

            	@Column(name="`SUGAR-4-TIME`")
            	private String sugar_4_time;

            	@Column(name="`SUGAR-4-VALUE`")
            	private BigDecimal sugar_4_value;

            	@Column(name="`SUGAR-5-TIME`")
            	private String sugar_5_time;

            	@Column(name="`SUGAR-5-VALUE`")
            	private BigDecimal sugar_5_value;

            	@Column(name="`SUGAR-6-TIME`")
            	private String sugar_6_time;

            	@Column(name="`SUGAR-6-VALUE`")
            	private BigDecimal sugar_6_value;

            	@Column(name="`SUGAR-7-TIME`")
            	private String sugar_7_time;

            	@Column(name="`SUGAR-7-VALUE`")
            	private BigDecimal sugar_7_value;

            	@Column(name="`SUGAR-8-TIME`")
            	private String sugar_8_time;

            	@Column(name="`SUGAR-8-VALUE`")
            	private BigDecimal sugar_8_value;

            	@Column(name="`SUGAR-9-TIME`")
            	private String sugar_9_time;

            	@Column(name="`SUGAR-9-VALUE`")
            	private BigDecimal sugar_9_value;

            	private String updatedBy;

            	@Temporal(TemporalType.TIMESTAMP)
            	private Date updatedTime;
            	
            	
                public Integer getAuditReportID() {
                                return auditReportID;
                }

                public void setAuditReportID(Integer auditReportID) {
                                this.auditReportID = auditReportID;
                }

                public String getAbg_1_bb() {
                                return abg_1_bb;
                }

                public void setAbg_1_bb(String abg_1_bb) {
                                this.abg_1_bb = abg_1_bb;
                }

                public String getAbg_1_be() {
                                return abg_1_be;
                }

                public void setAbg_1_be(String abg_1_be) {
                                this.abg_1_be = abg_1_be;
                }

                public String getAbg_1_ca2() {
                                return abg_1_ca2;
                }

                public void setAbg_1_ca2(String abg_1_ca2) {
                                this.abg_1_ca2 = abg_1_ca2;
                }

                public String getAbg_1_cl() {
                                return abg_1_cl;
                }

                public void setAbg_1_cl(String abg_1_cl) {
                                this.abg_1_cl = abg_1_cl;
                }

                public String getAbg_1_fio2() {
                                return abg_1_fio2;
                }

                public void setAbg_1_fio2(String abg_1_fio2) {
                                this.abg_1_fio2 = abg_1_fio2;
                }

                public String getAbg_1_hco3() {
                                return abg_1_hco3;
                }

                public void setAbg_1_hco3(String abg_1_hco3) {
                                this.abg_1_hco3 = abg_1_hco3;
                }

                public String getAbg_1_hct() {
                                return abg_1_hct;
                }

                public void setAbg_1_hct(String abg_1_hct) {
                                this.abg_1_hct = abg_1_hct;
                }

                public String getAbg_1_k() {
                                return abg_1_k;
                }

                public void setAbg_1_k(String abg_1_k) {
                                this.abg_1_k = abg_1_k;
                }

                public String getAbg_1_lactate() {
                                return abg_1_lactate;
                }

                public void setAbg_1_lactate(String abg_1_lactate) {
                                this.abg_1_lactate = abg_1_lactate;
                }

                public String getAbg_1_na() {
                                return abg_1_na;
                }

                public void setAbg_1_na(String abg_1_na) {
                                this.abg_1_na = abg_1_na;
                }

                public String getAbg_1_others() {
                                return abg_1_others;
                }

                public void setAbg_1_others(String abg_1_others) {
                                this.abg_1_others = abg_1_others;
                }

                public String getAbg_1_pco2() {
                                return abg_1_pco2;
                }

                public void setAbg_1_pco2(String abg_1_pco2) {
                                this.abg_1_pco2 = abg_1_pco2;
                }

                public String getAbg_1_ph() {
                                return abg_1_ph;
                }

                public void setAbg_1_ph(String abg_1_ph) {
                                this.abg_1_ph = abg_1_ph;
                }

                public String getAbg_1_spo2() {
                                return abg_1_spo2;
                }

                public void setAbg_1_spo2(String abg_1_spo2) {
                                this.abg_1_spo2 = abg_1_spo2;
                }

                public String getAbg_1_time() {
                                return abg_1_time;
                }

                public void setAbg_1_time(String abg_1_time) {
                                this.abg_1_time = abg_1_time;
                }

                public String getAbg_10_bb() {
                                return abg_10_bb;
                }

                public void setAbg_10_bb(String abg_10_bb) {
                                this.abg_10_bb = abg_10_bb;
                }

                public String getAbg_10_be() {
                                return abg_10_be;
                }

                public void setAbg_10_be(String abg_10_be) {
                                this.abg_10_be = abg_10_be;
                }

                public String getAbg_10_ca2() {
                                return abg_10_ca2;
                }

                public void setAbg_10_ca2(String abg_10_ca2) {
                                this.abg_10_ca2 = abg_10_ca2;
                }

                public String getAbg_10_cl() {
                                return abg_10_cl;
                }

                public void setAbg_10_cl(String abg_10_cl) {
                                this.abg_10_cl = abg_10_cl;
                }

                public String getAbg_10_fio2() {
                                return abg_10_fio2;
                }

                public void setAbg_10_fio2(String abg_10_fio2) {
                                this.abg_10_fio2 = abg_10_fio2;
                }

                public String getAbg_10_hco3() {
                                return abg_10_hco3;
                }

                public void setAbg_10_hco3(String abg_10_hco3) {
                                this.abg_10_hco3 = abg_10_hco3;
                }

                public String getAbg_10_hct() {
                                return abg_10_hct;
                }

                public void setAbg_10_hct(String abg_10_hct) {
                                this.abg_10_hct = abg_10_hct;
                }

                public String getAbg_10_k() {
                                return abg_10_k;
                }

                public void setAbg_10_k(String abg_10_k) {
                                this.abg_10_k = abg_10_k;
                }

                public String getAbg_10_lactate() {
                                return abg_10_lactate;
                }

                public void setAbg_10_lactate(String abg_10_lactate) {
                                this.abg_10_lactate = abg_10_lactate;
                }

                public String getAbg_10_na() {
                                return abg_10_na;
                }

                public void setAbg_10_na(String abg_10_na) {
                                this.abg_10_na = abg_10_na;
                }

                public String getAbg_10_others() {
                                return abg_10_others;
                }

                public void setAbg_10_others(String abg_10_others) {
                                this.abg_10_others = abg_10_others;
                }

                public String getAbg_10_pco2() {
                                return abg_10_pco2;
                }

                public void setAbg_10_pco2(String abg_10_pco2) {
                                this.abg_10_pco2 = abg_10_pco2;
                }

                public String getAbg_10_ph() {
                                return abg_10_ph;
                }

                public void setAbg_10_ph(String abg_10_ph) {
                                this.abg_10_ph = abg_10_ph;
                }

                public String getAbg_10_spo2() {
                                return abg_10_spo2;
                }

                public void setAbg_10_spo2(String abg_10_spo2) {
                                this.abg_10_spo2 = abg_10_spo2;
                }

                public String getAbg_10_time() {
                                return abg_10_time;
                }

                public void setAbg_10_time(String abg_10_time) {
                                this.abg_10_time = abg_10_time;
                }

                public String getAbg_2_bb() {
                                return abg_2_bb;
                }

                public void setAbg_2_bb(String abg_2_bb) {
                                this.abg_2_bb = abg_2_bb;
                }

                public String getAbg_2_be() {
                                return abg_2_be;
                }

                public void setAbg_2_be(String abg_2_be) {
                                this.abg_2_be = abg_2_be;
                }

                public String getAbg_2_ca2() {
                                return abg_2_ca2;
                }

                public void setAbg_2_ca2(String abg_2_ca2) {
                                this.abg_2_ca2 = abg_2_ca2;
                }

                public String getAbg_2_cl() {
                                return abg_2_cl;
                }

                public void setAbg_2_cl(String abg_2_cl) {
                                this.abg_2_cl = abg_2_cl;
                }

                public String getAbg_2_fio2() {
                                return abg_2_fio2;
                }

                public void setAbg_2_fio2(String abg_2_fio2) {
                                this.abg_2_fio2 = abg_2_fio2;
                }

                public String getAbg_2_hco3() {
                                return abg_2_hco3;
                }

                public void setAbg_2_hco3(String abg_2_hco3) {
                                this.abg_2_hco3 = abg_2_hco3;
                }

                public String getAbg_2_hct() {
                                return abg_2_hct;
                }

                public void setAbg_2_hct(String abg_2_hct) {
                                this.abg_2_hct = abg_2_hct;
                }

                public String getAbg_2_k() {
                                return abg_2_k;
                }

                public void setAbg_2_k(String abg_2_k) {
                                this.abg_2_k = abg_2_k;
                }

                public String getAbg_2_lactate() {
                                return abg_2_lactate;
                }

                public void setAbg_2_lactate(String abg_2_lactate) {
                                this.abg_2_lactate = abg_2_lactate;
                }

                public String getAbg_2_na() {
                                return abg_2_na;
                }

                public void setAbg_2_na(String abg_2_na) {
                                this.abg_2_na = abg_2_na;
                }

                public String getAbg_2_others() {
                                return abg_2_others;
                }

                public void setAbg_2_others(String abg_2_others) {
                                this.abg_2_others = abg_2_others;
                }

                public String getAbg_2_pco2() {
                                return abg_2_pco2;
                }

                public void setAbg_2_pco2(String abg_2_pco2) {
                                this.abg_2_pco2 = abg_2_pco2;
                }

                public String getAbg_2_ph() {
                                return abg_2_ph;
                }

                public void setAbg_2_ph(String abg_2_ph) {
                                this.abg_2_ph = abg_2_ph;
                }

                public String getAbg_2_spo2() {
                                return abg_2_spo2;
                }

                public void setAbg_2_spo2(String abg_2_spo2) {
                                this.abg_2_spo2 = abg_2_spo2;
                }

                public String getAbg_2_time() {
                                return abg_2_time;
                }

                public void setAbg_2_time(String abg_2_time) {
                                this.abg_2_time = abg_2_time;
                }

                public String getAbg_3_bb() {
                                return abg_3_bb;
                }

                public void setAbg_3_bb(String abg_3_bb) {
                                this.abg_3_bb = abg_3_bb;
                }

                public String getAbg_3_be() {
                                return abg_3_be;
                }

                public void setAbg_3_be(String abg_3_be) {
                                this.abg_3_be = abg_3_be;
                }

                public String getAbg_3_ca2() {
                                return abg_3_ca2;
                }

                public void setAbg_3_ca2(String abg_3_ca2) {
                                this.abg_3_ca2 = abg_3_ca2;
                }

                public String getAbg_3_cl() {
                                return abg_3_cl;
                }

                public void setAbg_3_cl(String abg_3_cl) {
                                this.abg_3_cl = abg_3_cl;
                }

                public String getAbg_3_fio2() {
                                return abg_3_fio2;
                }

                public void setAbg_3_fio2(String abg_3_fio2) {
                                this.abg_3_fio2 = abg_3_fio2;
                }

                public String getAbg_3_hco3() {
                                return abg_3_hco3;
                }

                public void setAbg_3_hco3(String abg_3_hco3) {
                                this.abg_3_hco3 = abg_3_hco3;
                }

                public String getAbg_3_hct() {
                                return abg_3_hct;
                }

                public void setAbg_3_hct(String abg_3_hct) {
                                this.abg_3_hct = abg_3_hct;
                }

                public String getAbg_3_k() {
                                return abg_3_k;
                }

                public void setAbg_3_k(String abg_3_k) {
                                this.abg_3_k = abg_3_k;
                }

                public String getAbg_3_lactate() {
                                return abg_3_lactate;
                }

                public void setAbg_3_lactate(String abg_3_lactate) {
                                this.abg_3_lactate = abg_3_lactate;
                }

                public String getAbg_3_na() {
                                return abg_3_na;
                }

                public void setAbg_3_na(String abg_3_na) {
                                this.abg_3_na = abg_3_na;
                }

                public String getAbg_3_others() {
                                return abg_3_others;
                }

                public void setAbg_3_others(String abg_3_others) {
                                this.abg_3_others = abg_3_others;
                }

                public String getAbg_3_pco2() {
                                return abg_3_pco2;
                }

                public void setAbg_3_pco2(String abg_3_pco2) {
                                this.abg_3_pco2 = abg_3_pco2;
                }

                public String getAbg_3_ph() {
                                return abg_3_ph;
                }

                public void setAbg_3_ph(String abg_3_ph) {
                                this.abg_3_ph = abg_3_ph;
                }

                public String getAbg_3_spo2() {
                                return abg_3_spo2;
                }

                public void setAbg_3_spo2(String abg_3_spo2) {
                                this.abg_3_spo2 = abg_3_spo2;
                }

                public String getAbg_3_time() {
                                return abg_3_time;
                }

                public void setAbg_3_time(String abg_3_time) {
                                this.abg_3_time = abg_3_time;
                }

                public String getAbg_4_bb() {
                                return abg_4_bb;
                }

                public void setAbg_4_bb(String abg_4_bb) {
                                this.abg_4_bb = abg_4_bb;
                }

                public String getAbg_4_be() {
                                return abg_4_be;
                }

                public void setAbg_4_be(String abg_4_be) {
                                this.abg_4_be = abg_4_be;
                }

                public String getAbg_4_ca2() {
                                return abg_4_ca2;
                }

                public void setAbg_4_ca2(String abg_4_ca2) {
                                this.abg_4_ca2 = abg_4_ca2;
                }

                public String getAbg_4_cl() {
                                return abg_4_cl;
                }

                public void setAbg_4_cl(String abg_4_cl) {
                                this.abg_4_cl = abg_4_cl;
                }

                public String getAbg_4_fio2() {
                                return abg_4_fio2;
                }

                public void setAbg_4_fio2(String abg_4_fio2) {
                                this.abg_4_fio2 = abg_4_fio2;
                }

                public String getAbg_4_hco3() {
                                return abg_4_hco3;
                }

                public void setAbg_4_hco3(String abg_4_hco3) {
                                this.abg_4_hco3 = abg_4_hco3;
                }

                public String getAbg_4_hct() {
                                return abg_4_hct;
                }

                public void setAbg_4_hct(String abg_4_hct) {
                                this.abg_4_hct = abg_4_hct;
                }

                public String getAbg_4_k() {
                                return abg_4_k;
                }

                public void setAbg_4_k(String abg_4_k) {
                                this.abg_4_k = abg_4_k;
                }

                public String getAbg_4_lactate() {
                                return abg_4_lactate;
                }

                public void setAbg_4_lactate(String abg_4_lactate) {
                                this.abg_4_lactate = abg_4_lactate;
                }

                public String getAbg_4_na() {
                                return abg_4_na;
                }

                public void setAbg_4_na(String abg_4_na) {
                                this.abg_4_na = abg_4_na;
                }

                public String getAbg_4_others() {
                                return abg_4_others;
                }

                public void setAbg_4_others(String abg_4_others) {
                                this.abg_4_others = abg_4_others;
                }

                public String getAbg_4_pco2() {
                                return abg_4_pco2;
                }

                public void setAbg_4_pco2(String abg_4_pco2) {
                                this.abg_4_pco2 = abg_4_pco2;
                }

                public String getAbg_4_ph() {
                                return abg_4_ph;
                }

                public void setAbg_4_ph(String abg_4_ph) {
                                this.abg_4_ph = abg_4_ph;
                }

                public String getAbg_4_spo2() {
                                return abg_4_spo2;
                }

                public void setAbg_4_spo2(String abg_4_spo2) {
                                this.abg_4_spo2 = abg_4_spo2;
                }

                public String getAbg_4_time() {
                                return abg_4_time;
                }

                public void setAbg_4_time(String abg_4_time) {
                                this.abg_4_time = abg_4_time;
                }

                public String getAbg_5_bb() {
                                return abg_5_bb;
                }

                public void setAbg_5_bb(String abg_5_bb) {
                                this.abg_5_bb = abg_5_bb;
                }

                public String getAbg_5_be() {
                                return abg_5_be;
                }

                public void setAbg_5_be(String abg_5_be) {
                                this.abg_5_be = abg_5_be;
                }

                public String getAbg_5_ca2() {
                                return abg_5_ca2;
                }

                public void setAbg_5_ca2(String abg_5_ca2) {
                                this.abg_5_ca2 = abg_5_ca2;
                }

                public String getAbg_5_cl() {
                                return abg_5_cl;
                }

                public void setAbg_5_cl(String abg_5_cl) {
                                this.abg_5_cl = abg_5_cl;
                }

                public String getAbg_5_fio2() {
                                return abg_5_fio2;
                }

                public void setAbg_5_fio2(String abg_5_fio2) {
                                this.abg_5_fio2 = abg_5_fio2;
                }

                public String getAbg_5_hco3() {
                                return abg_5_hco3;
                }

                public void setAbg_5_hco3(String abg_5_hco3) {
                                this.abg_5_hco3 = abg_5_hco3;
                }

                public String getAbg_5_hct() {
                                return abg_5_hct;
                }

                public void setAbg_5_hct(String abg_5_hct) {
                                this.abg_5_hct = abg_5_hct;
                }

                public String getAbg_5_k() {
                                return abg_5_k;
                }

                public void setAbg_5_k(String abg_5_k) {
                                this.abg_5_k = abg_5_k;
                }

                public String getAbg_5_lactate() {
                                return abg_5_lactate;
                }

                public void setAbg_5_lactate(String abg_5_lactate) {
                                this.abg_5_lactate = abg_5_lactate;
                }

                public String getAbg_5_na() {
                                return abg_5_na;
                }

                public void setAbg_5_na(String abg_5_na) {
                                this.abg_5_na = abg_5_na;
                }

                public String getAbg_5_others() {
                                return abg_5_others;
                }

                public void setAbg_5_others(String abg_5_others) {
                                this.abg_5_others = abg_5_others;
                }

                public String getAbg_5_pco2() {
                                return abg_5_pco2;
                }

                public void setAbg_5_pco2(String abg_5_pco2) {
                                this.abg_5_pco2 = abg_5_pco2;
                }

                public String getAbg_5_ph() {
                                return abg_5_ph;
                }

                public void setAbg_5_ph(String abg_5_ph) {
                                this.abg_5_ph = abg_5_ph;
                }

                public String getAbg_5_spo2() {
                                return abg_5_spo2;
                }

                public void setAbg_5_spo2(String abg_5_spo2) {
                                this.abg_5_spo2 = abg_5_spo2;
                }

                public String getAbg_5_time() {
                                return abg_5_time;
                }

                public void setAbg_5_time(String abg_5_time) {
                                this.abg_5_time = abg_5_time;
                }

                public String getAbg_6_bb() {
                                return abg_6_bb;
                }

                public void setAbg_6_bb(String abg_6_bb) {
                                this.abg_6_bb = abg_6_bb;
                }

                public String getAbg_6_be() {
                                return abg_6_be;
                }

                public void setAbg_6_be(String abg_6_be) {
                                this.abg_6_be = abg_6_be;
                }

                public String getAbg_6_ca2() {
                                return abg_6_ca2;
                }

                public void setAbg_6_ca2(String abg_6_ca2) {
                                this.abg_6_ca2 = abg_6_ca2;
                }

                public String getAbg_6_cl() {
                                return abg_6_cl;
                }

                public void setAbg_6_cl(String abg_6_cl) {
                                this.abg_6_cl = abg_6_cl;
                }

                public String getAbg_6_fio2() {
                                return abg_6_fio2;
                }

                public void setAbg_6_fio2(String abg_6_fio2) {
                                this.abg_6_fio2 = abg_6_fio2;
                }

                public String getAbg_6_hco3() {
                                return abg_6_hco3;
                }

                public void setAbg_6_hco3(String abg_6_hco3) {
                                this.abg_6_hco3 = abg_6_hco3;
                }

                public String getAbg_6_hct() {
                                return abg_6_hct;
                }

                public void setAbg_6_hct(String abg_6_hct) {
                                this.abg_6_hct = abg_6_hct;
                }

                public String getAbg_6_k() {
                                return abg_6_k;
                }

                public void setAbg_6_k(String abg_6_k) {
                                this.abg_6_k = abg_6_k;
                }

                public String getAbg_6_lactate() {
                                return abg_6_lactate;
                }

                public void setAbg_6_lactate(String abg_6_lactate) {
                                this.abg_6_lactate = abg_6_lactate;
                }

                public String getAbg_6_na() {
                                return abg_6_na;
                }

                public void setAbg_6_na(String abg_6_na) {
                                this.abg_6_na = abg_6_na;
                }

                public String getAbg_6_others() {
                                return abg_6_others;
                }

                public void setAbg_6_others(String abg_6_others) {
                                this.abg_6_others = abg_6_others;
                }

                public String getAbg_6_pco2() {
                                return abg_6_pco2;
                }

                public void setAbg_6_pco2(String abg_6_pco2) {
                                this.abg_6_pco2 = abg_6_pco2;
                }

                public String getAbg_6_ph() {
                                return abg_6_ph;
                }

                public void setAbg_6_ph(String abg_6_ph) {
                                this.abg_6_ph = abg_6_ph;
                }

                public String getAbg_6_spo2() {
                                return abg_6_spo2;
                }

                public void setAbg_6_spo2(String abg_6_spo2) {
                                this.abg_6_spo2 = abg_6_spo2;
                }

                public String getAbg_6_time() {
                                return abg_6_time;
                }

                public void setAbg_6_time(String abg_6_time) {
                                this.abg_6_time = abg_6_time;
                }

                public String getAbg_7_bb() {
                                return abg_7_bb;
                }

                public void setAbg_7_bb(String abg_7_bb) {
                                this.abg_7_bb = abg_7_bb;
                }

                public String getAbg_7_be() {
                                return abg_7_be;
                }

                public void setAbg_7_be(String abg_7_be) {
                                this.abg_7_be = abg_7_be;
                }

                public String getAbg_7_ca2() {
                                return abg_7_ca2;
                }

                public void setAbg_7_ca2(String abg_7_ca2) {
                                this.abg_7_ca2 = abg_7_ca2;
                }

                public String getAbg_7_cl() {
                                return abg_7_cl;
                }

                public void setAbg_7_cl(String abg_7_cl) {
                                this.abg_7_cl = abg_7_cl;
                }

                public String getAbg_7_fio2() {
                                return abg_7_fio2;
                }

                public void setAbg_7_fio2(String abg_7_fio2) {
                                this.abg_7_fio2 = abg_7_fio2;
                }

                public String getAbg_7_hco3() {
                                return abg_7_hco3;
                }

                public void setAbg_7_hco3(String abg_7_hco3) {
                                this.abg_7_hco3 = abg_7_hco3;
                }

                public String getAbg_7_hct() {
                                return abg_7_hct;
                }

                public void setAbg_7_hct(String abg_7_hct) {
                                this.abg_7_hct = abg_7_hct;
                }

                public String getAbg_7_k() {
                                return abg_7_k;
                }

                public void setAbg_7_k(String abg_7_k) {
                                this.abg_7_k = abg_7_k;
                }

                public String getAbg_7_lactate() {
                                return abg_7_lactate;
                }

                public void setAbg_7_lactate(String abg_7_lactate) {
                                this.abg_7_lactate = abg_7_lactate;
                }

                public String getAbg_7_na() {
                                return abg_7_na;
                }

                public void setAbg_7_na(String abg_7_na) {
                                this.abg_7_na = abg_7_na;
                }

                public String getAbg_7_others() {
                                return abg_7_others;
                }

                public void setAbg_7_others(String abg_7_others) {
                                this.abg_7_others = abg_7_others;
                }

                public String getAbg_7_pco2() {
                                return abg_7_pco2;
                }

                public void setAbg_7_pco2(String abg_7_pco2) {
                                this.abg_7_pco2 = abg_7_pco2;
                }

                public String getAbg_7_ph() {
                                return abg_7_ph;
                }

                public void setAbg_7_ph(String abg_7_ph) {
                                this.abg_7_ph = abg_7_ph;
                }

                public String getAbg_7_spo2() {
                                return abg_7_spo2;
                }

                public void setAbg_7_spo2(String abg_7_spo2) {
                                this.abg_7_spo2 = abg_7_spo2;
                }

                public String getAbg_7_time() {
                                return abg_7_time;
                }

                public void setAbg_7_time(String abg_7_time) {
                                this.abg_7_time = abg_7_time;
                }

                public String getAbg_8_bb() {
                                return abg_8_bb;
                }

                public void setAbg_8_bb(String abg_8_bb) {
                                this.abg_8_bb = abg_8_bb;
                }

                public String getAbg_8_be() {
                                return abg_8_be;
                }

                public void setAbg_8_be(String abg_8_be) {
                                this.abg_8_be = abg_8_be;
                }

                public String getAbg_8_ca2() {
                                return abg_8_ca2;
                }

                public void setAbg_8_ca2(String abg_8_ca2) {
                                this.abg_8_ca2 = abg_8_ca2;
                }

                public String getAbg_8_cl() {
                                return abg_8_cl;
                }

                public void setAbg_8_cl(String abg_8_cl) {
                                this.abg_8_cl = abg_8_cl;
                }

                public String getAbg_8_fio2() {
                                return abg_8_fio2;
                }

                public void setAbg_8_fio2(String abg_8_fio2) {
                                this.abg_8_fio2 = abg_8_fio2;
                }

                public String getAbg_8_hco3() {
                                return abg_8_hco3;
                }

                public void setAbg_8_hco3(String abg_8_hco3) {
                                this.abg_8_hco3 = abg_8_hco3;
                }

                public String getAbg_8_hct() {
                                return abg_8_hct;
                }

                public void setAbg_8_hct(String abg_8_hct) {
                                this.abg_8_hct = abg_8_hct;
                }

                public String getAbg_8_k() {
                                return abg_8_k;
                }

                public void setAbg_8_k(String abg_8_k) {
                                this.abg_8_k = abg_8_k;
                }

                public String getAbg_8_lactate() {
                                return abg_8_lactate;
                }

                public void setAbg_8_lactate(String abg_8_lactate) {
                                this.abg_8_lactate = abg_8_lactate;
                }

                public String getAbg_8_na() {
                                return abg_8_na;
                }

                public void setAbg_8_na(String abg_8_na) {
                                this.abg_8_na = abg_8_na;
                }

                public String getAbg_8_others() {
                                return abg_8_others;
                }

                public void setAbg_8_others(String abg_8_others) {
                                this.abg_8_others = abg_8_others;
                }

                public String getAbg_8_pco2() {
                                return abg_8_pco2;
                }

                public void setAbg_8_pco2(String abg_8_pco2) {
                                this.abg_8_pco2 = abg_8_pco2;
                }

                public String getAbg_8_ph() {
                                return abg_8_ph;
                }

                public void setAbg_8_ph(String abg_8_ph) {
                                this.abg_8_ph = abg_8_ph;
                }

                public String getAbg_8_spo2() {
                                return abg_8_spo2;
                }

                public void setAbg_8_spo2(String abg_8_spo2) {
                                this.abg_8_spo2 = abg_8_spo2;
                }

                public String getAbg_8_time() {
                                return abg_8_time;
                }

                public void setAbg_8_time(String abg_8_time) {
                                this.abg_8_time = abg_8_time;
                }

                public String getAbg_9_bb() {
                                return abg_9_bb;
                }

                public void setAbg_9_bb(String abg_9_bb) {
                                this.abg_9_bb = abg_9_bb;
                }

                public String getAbg_9_be() {
                                return abg_9_be;
                }

                public void setAbg_9_be(String abg_9_be) {
                                this.abg_9_be = abg_9_be;
                }

                public String getAbg_9_ca2() {
                                return abg_9_ca2;
                }

                public void setAbg_9_ca2(String abg_9_ca2) {
                                this.abg_9_ca2 = abg_9_ca2;
                }

                public String getAbg_9_cl() {
                                return abg_9_cl;
                }

                public void setAbg_9_cl(String abg_9_cl) {
                                this.abg_9_cl = abg_9_cl;
                }

                public String getAbg_9_fio2() {
                                return abg_9_fio2;
                }

                public void setAbg_9_fio2(String abg_9_fio2) {
                                this.abg_9_fio2 = abg_9_fio2;
                }

                public String getAbg_9_hco3() {
                                return abg_9_hco3;
                }

                public void setAbg_9_hco3(String abg_9_hco3) {
                                this.abg_9_hco3 = abg_9_hco3;
                }

                public String getAbg_9_hct() {
                                return abg_9_hct;
                }

                public void setAbg_9_hct(String abg_9_hct) {
                                this.abg_9_hct = abg_9_hct;
                }

                public String getAbg_9_k() {
                                return abg_9_k;
                }

                public void setAbg_9_k(String abg_9_k) {
                                this.abg_9_k = abg_9_k;
                }

                public String getAbg_9_lactate() {
                                return abg_9_lactate;
                }

                public void setAbg_9_lactate(String abg_9_lactate) {
                                this.abg_9_lactate = abg_9_lactate;
                }

                public String getAbg_9_na() {
                                return abg_9_na;
                }

                public void setAbg_9_na(String abg_9_na) {
                                this.abg_9_na = abg_9_na;
                }

                public String getAbg_9_others() {
                                return abg_9_others;
                }

                public void setAbg_9_others(String abg_9_others) {
                                this.abg_9_others = abg_9_others;
                }

                public String getAbg_9_pco2() {
                                return abg_9_pco2;
                }

                public void setAbg_9_pco2(String abg_9_pco2) {
                                this.abg_9_pco2 = abg_9_pco2;
                }

                public String getAbg_9_ph() {
                                return abg_9_ph;
                }

                public void setAbg_9_ph(String abg_9_ph) {
                                this.abg_9_ph = abg_9_ph;
                }

                public String getAbg_9_spo2() {
                                return abg_9_spo2;
                }

                public void setAbg_9_spo2(String abg_9_spo2) {
                                this.abg_9_spo2 = abg_9_spo2;
                }

                public String getAbg_9_time() {
                                return abg_9_time;
                }

                public void setAbg_9_time(String abg_9_time) {
                                this.abg_9_time = abg_9_time;
                }

                public String getAct_1_time() {
                                return act_1_time;
                }

                public void setAct_1_time(String act_1_time) {
                                this.act_1_time = act_1_time;
                }

                public BigDecimal getAct_1_value() {
                                return act_1_value;
                }

                public void setAct_1_value(BigDecimal act_1_value) {
                                this.act_1_value = act_1_value;
                }

                public String getAct_10_time() {
                                return act_10_time;
                }

                public void setAct_10_time(String act_10_time) {
                                this.act_10_time = act_10_time;
                }

                public BigDecimal getAct_10_value() {
                                return act_10_value;
                }

                public void setAct_10_value(BigDecimal act_10_value) {
                                this.act_10_value = act_10_value;
                }

                public String getAct_2_time() {
                                return act_2_time;
                }

                public void setAct_2_time(String act_2_time) {
                                this.act_2_time = act_2_time;
                }

                public BigDecimal getAct_2_value() {
                                return act_2_value;
                }

                public void setAct_2_value(BigDecimal act_2_value) {
                                this.act_2_value = act_2_value;
                }

                public String getAct_3_time() {
                                return act_3_time;
                }

                public void setAct_3_time(String act_3_time) {
                                this.act_3_time = act_3_time;
                }

                public BigDecimal getAct_3_value() {
                                return act_3_value;
                }

                public void setAct_3_value(BigDecimal act_3_value) {
                                this.act_3_value = act_3_value;
                }

                public String getAct_4_time() {
                                return act_4_time;
                }

                public void setAct_4_time(String act_4_time) {
                                this.act_4_time = act_4_time;
                }

                public BigDecimal getAct_4_value() {
                                return act_4_value;
                }

                public void setAct_4_value(BigDecimal act_4_value) {
                                this.act_4_value = act_4_value;
                }

                public String getAct_5_time() {
                                return act_5_time;
                }

                public void setAct_5_time(String act_5_time) {
                                this.act_5_time = act_5_time;
                }

                public BigDecimal getAct_5_value() {
                                return act_5_value;
                }

                public void setAct_5_value(BigDecimal act_5_value) {
                                this.act_5_value = act_5_value;
                }

                public String getAct_6_time() {
                                return act_6_time;
                }

                public void setAct_6_time(String act_6_time) {
                                this.act_6_time = act_6_time;
                }

                public BigDecimal getAct_6_value() {
                                return act_6_value;
                }

                public void setAct_6_value(BigDecimal act_6_value) {
                                this.act_6_value = act_6_value;
                }

                public String getAct_7_time() {
                                return act_7_time;
                }

                public void setAct_7_time(String act_7_time) {
                                this.act_7_time = act_7_time;
                }

                public BigDecimal getAct_7_value() {
                                return act_7_value;
                }

                public void setAct_7_value(BigDecimal act_7_value) {
                                this.act_7_value = act_7_value;
                }

                public String getAct_8_time() {
                                return act_8_time;
                }

                public void setAct_8_time(String act_8_time) {
                                this.act_8_time = act_8_time;
                }

                public BigDecimal getAct_8_value() {
                                return act_8_value;
                }

                public void setAct_8_value(BigDecimal act_8_value) {
                                this.act_8_value = act_8_value;
                }

                public String getAct_9_time() {
                                return act_9_time;
                }

                public void setAct_9_time(String act_9_time) {
                                this.act_9_time = act_9_time;
                }

                public BigDecimal getAct_9_value() {
                                return act_9_value;
                }

                public void setAct_9_value(BigDecimal act_9_value) {
                                this.act_9_value = act_9_value;
                }

               

                public Long getCaseID() {
					return caseID;
				}

				public void setCaseID(Long caseID) {
					this.caseID = caseID;
				}

				public String getCreatedBy() {
                                return createdBy;
                }

                public void setCreatedBy(String createdBy) {
                                this.createdBy = createdBy;
                }

                public Date getCreatedTime() {
                                return createdTime;
                }

                public void setCreatedTime(Date createdTime) {
                                this.createdTime = createdTime;
                }         

                public Float getFluid_NS() {
                                return fluid_NS;
                }

                public void setFluid_NS(Float fluid_NS) {
                                this.fluid_NS = fluid_NS;
                }

                public Float getFluid_Other() {
                                return fluid_Other;
                }

                public void setFluid_Other(Float fluid_Other) {
                                this.fluid_Other = fluid_Other;
                }

                public Float getFluid_RL() {
                                return fluid_RL;
                }

                public void setFluid_RL(Float fluid_RL) {
                                this.fluid_RL = fluid_RL;
                }

               

                public String getOutput_Blood() {
                                return output_Blood;
                }

                public void setOutput_Blood(String output_Blood) {
                                this.output_Blood = output_Blood;
                }

                public String getOutput_Urine() {
                                return output_Urine;
                }

                public void setOutput_Urine(String output_Urine) {
                                this.output_Urine = output_Urine;
                }


                public String getSugar_1_time() {
                                return sugar_1_time;
                }

                public void setSugar_1_time(String sugar_1_time) {
                                this.sugar_1_time = sugar_1_time;
                }

                public BigDecimal getSugar_1_value() {
                                return sugar_1_value;
                }

                public void setSugar_1_value(BigDecimal sugar_1_value) {
                                this.sugar_1_value = sugar_1_value;
                }

                public String getSugar_10_time() {
                                return sugar_10_time;
                }

                public void setSugar_10_time(String sugar_10_time) {
                                this.sugar_10_time = sugar_10_time;
                }

                public BigDecimal getSugar_10_value() {
                                return sugar_10_value;
                }

                public void setSugar_10_value(BigDecimal sugar_10_value) {
                                this.sugar_10_value = sugar_10_value;
                }

                public String getSugar_2_time() {
                                return sugar_2_time;
                }

                public void setSugar_2_time(String sugar_2_time) {
                                this.sugar_2_time = sugar_2_time;
                }

                public BigDecimal getSugar_2_value() {
                                return sugar_2_value;
                }

                public void setSugar_2_value(BigDecimal sugar_2_value) {
                                this.sugar_2_value = sugar_2_value;
                }

                public String getSugar_3_time() {
                                return sugar_3_time;
                }

                public void setSugar_3_time(String sugar_3_time) {
                                this.sugar_3_time = sugar_3_time;
                }

                public BigDecimal getSugar_3_value() {
                                return sugar_3_value;
                }

                public void setSugar_3_value(BigDecimal sugar_3_value) {
                                this.sugar_3_value = sugar_3_value;
                }

                public String getSugar_4_time() {
                                return sugar_4_time;
                }

                public void setSugar_4_time(String sugar_4_time) {
                                this.sugar_4_time = sugar_4_time;
                }

                public BigDecimal getSugar_4_value() {
                                return sugar_4_value;
                }

                public void setSugar_4_value(BigDecimal sugar_4_value) {
                                this.sugar_4_value = sugar_4_value;
                }

                public String getSugar_5_time() {
                                return sugar_5_time;
                }

                public void setSugar_5_time(String sugar_5_time) {
                                this.sugar_5_time = sugar_5_time;
                }

                public BigDecimal getSugar_5_value() {
                                return sugar_5_value;
                }

                public void setSugar_5_value(BigDecimal sugar_5_value) {
                                this.sugar_5_value = sugar_5_value;
                }

                public String getSugar_6_time() {
                                return sugar_6_time;
                }

                public void setSugar_6_time(String sugar_6_time) {
                                this.sugar_6_time = sugar_6_time;
                }

                public BigDecimal getSugar_6_value() {
                                return sugar_6_value;
                }

                public void setSugar_6_value(BigDecimal sugar_6_value) {
                                this.sugar_6_value = sugar_6_value;
                }

                public String getSugar_7_time() {
                                return sugar_7_time;
                }

                public void setSugar_7_time(String sugar_7_time) {
                                this.sugar_7_time = sugar_7_time;
                }

                public BigDecimal getSugar_7_value() {
                                return sugar_7_value;
                }

                public void setSugar_7_value(BigDecimal sugar_7_value) {
                                this.sugar_7_value = sugar_7_value;
                }

                public String getSugar_8_time() {
                                return sugar_8_time;
                }

                public void setSugar_8_time(String sugar_8_time) {
                                this.sugar_8_time = sugar_8_time;
                }

                public BigDecimal getSugar_8_value() {
                                return sugar_8_value;
                }

                public void setSugar_8_value(BigDecimal sugar_8_value) {
                                this.sugar_8_value = sugar_8_value;
                }

                public String getSugar_9_time() {
                                return sugar_9_time;
                }

                public void setSugar_9_time(String sugar_9_time) {
                                this.sugar_9_time = sugar_9_time;
                }

                public BigDecimal getSugar_9_value() {
                                return sugar_9_value;
                }

                public void setSugar_9_value(BigDecimal sugar_9_value) {
                                this.sugar_9_value = sugar_9_value;
                }             

                public String getUpdatedBy() {
                                return updatedBy;
                }

                public void setUpdatedBy(String updatedBy) {
                                this.updatedBy = updatedBy;
                }

                public Date getUpdatedTime() {
                                return updatedTime;
                }

                public void setUpdatedTime(Date updatedTime) {
                                this.updatedTime = updatedTime;
                }

				public String getAbg_2_po2() {
					return abg_2_po2;
				}

				public void setAbg_2_po2(String abg_2_po2) {
					this.abg_2_po2 = abg_2_po2;
				}

				public String getAbg_3_po2() {
					return abg_3_po2;
				}

				public void setAbg_3_po2(String abg_3_po2) {
					this.abg_3_po2 = abg_3_po2;
				}

				public String getAbg_4_po2() {
					return abg_4_po2;
				}

				public void setAbg_4_po2(String abg_4_po2) {
					this.abg_4_po2 = abg_4_po2;
				}

				public String getAbg_5_po2() {
					return abg_5_po2;
				}

				public void setAbg_5_po2(String abg_5_po2) {
					this.abg_5_po2 = abg_5_po2;
				}

				public String getAbg_6_po2() {
					return abg_6_po2;
				}

				public void setAbg_6_po2(String abg_6_po2) {
					this.abg_6_po2 = abg_6_po2;
				}

				public String getAbg_7_po2() {
					return abg_7_po2;
				}

				public void setAbg_7_po2(String abg_7_po2) {
					this.abg_7_po2 = abg_7_po2;
				}

				public String getAbg_8_po2() {
					return abg_8_po2;
				}

				public void setAbg_8_po2(String abg_8_po2) {
					this.abg_8_po2 = abg_8_po2;
				}

				public String getAbg_9_po2() {
					return abg_9_po2;
				}

				public void setAbg_9_po2(String abg_9_po2) {
					this.abg_9_po2 = abg_9_po2;
				}

				public String getAbg_10_po2() {
					return abg_10_po2;
				}

				public void setAbg_10_po2(String abg_10_po2) {
					this.abg_10_po2 = abg_10_po2;
				}

				public String getAbg_1_po2() {
					return abg_1_po2;
				}

				public void setAbg_1_po2(String abg_1_po2) {
					this.abg_1_po2 = abg_1_po2;
				}
                
                

}
