package Server;





	 class DatexRecordTypeDT
	{
		// Data validity macros
	    public static final int DATA_INVALID_LIMIT = -32001; // limit for special invalid data values
	    public static final int DATA_INVALID = -32767; // there is no valid data
	    public static final int DATA_NOT_UPDATED = -32766; // data is not updated
	    public static final int DATA_DISCONT = -32765; // data discontinuity (calibration ...)
	    public static final int DATA_UNDER_RANGE = -32764; // data exceeds lower valid limit
	    public static final int DATA_OVER_RANGE = -32763; // data exceeds upper valid limit
	    public static final int DATA_NOT_CALIBRATED = -32762; // data is not calibrated

	    // General definitions
	    // Asynchronous interface specific constants
	    public static final byte FRAMECHAR = 0x7E;
	    public static final byte CTRLCHAR = 0x7D;
	    public static final byte BIT5 = 0x7C;
	    public static final byte BIT5COMPL = 0x5F;

	    // Datex Record Interface data structure definitions
	    public static final int DRI_MAX_SUBRECS = 8; // # of subrecords in a packet
	    public static final int DRI_MAX_PHDBRECS = 5; // # of phys.db records in a packet

	    // data packet maintypes
	    public static final short DRI_MT_PHDB = 0; // Physiological data and related transmission requests.
	    public static final short DRI_MT_WAVE = 1; // Waveform data and related transmission requests.
	    public static final short DRI_MT_ALARM = 4; // Alarm data and related transmission requests.

	    // data packet subtypes
	    public static final byte DRI_PH_DISPL = 1;
	    public static final byte DRI_PH_10S_TREND = 2;
	    public static final byte DRI_PH_60S_TREND = 3;

	    public static final int DRI_PHDBCL_REQ_BASIC_MASK = 0x0000; // Enable sending of Basic physiological data class
	    public static final int DRI_PHDBCL_DENY_BASIC_MASK = 0x0001; // Disable sending of Basic physiological data class
	    public static final int DRI_PHDBCL_REQ_EXT1_MASK = 0x0002; // Enable sending of Ext1 physiological data class
	    public static final int DRI_PHDBCL_REQ_EXT2_MASK = 0x0004; // Enable sending of Ext2 physiological data class
	    public static final int DRI_PHDBCL_REQ_EXT3_MASK = 0x0008; // Enable sending of Ext3 physiological data class

	    // Datex Record Interface level types
	    public static final byte DRI_LEVEL_95 = 0x02;
	    public static final byte DRI_LEVEL_97 = 0x03;
	    public static final byte DRI_LEVEL_98 = 0x04;
	    public static final byte DRI_LEVEL_99 = 0x05;
	    public static final byte DRI_LEVEL_2000 = 0x06;
	    public static final byte DRI_LEVEL_2001 = 0x07;
	    public static final byte DRI_LEVEL_2003 = 0x08;
	    public static final byte DRI_LEVEL_2005 = 0x09;
	



}
	
	 class RecordHeader {
	        public short r_len;
	        public byte r_dri_level;
	        public int r_time;
	        public byte r_maintype;
	        public short sr_offset1;
	        public byte sr_type1;
	        public short sr_offset2;
	        public byte sr_type2;
	    }

	    class PhdbRecord {
	        public byte phdb_rcrd_type;
	        public short tx_interval;
	        public int phdb_class_bf;
	    }

	    class Request {
	        public RecordHeader hdr;
	        public PhdbRecord phdbr;

	        public Request() {
	            this.hdr = new RecordHeader();
	            this.phdbr = new PhdbRecord();
	        }
	    }
	    
	    
	    
	     class DatexRecordType1 {
	        DatexHdrType hdr = new DatexHdrType();
	        private byte[] data = new byte[1450];

	        // Getters and Setters for hdr
	        public DatexHdrType getHdr() {
	            return hdr;
	        }

	        public void setHdr(DatexHdrType hdr) {
	            this.hdr = hdr;
	        }

	        // Getters and Setters for data
	        public byte[] getData() {
	            return data;
	        }

	        public void setData(byte[] data) {
	            if (data.length != 1450) {
	                throw new IllegalArgumentException("Data array must be 1450 bytes in length");
	            }
	            this.data = data;
	        }
	    }
	    
	    
	     class DatexHdrType {
	        private short r_len;
	        private byte r_nbr;
	        private byte r_dri_level;
	        private int plug_id; // ushort in C# is an unsigned 16-bit integer; int in Java can represent it correctly
	        private long r_time; // uint in C# is an unsigned 32-bit integer; long in Java can represent it correctly
	        private byte reserved1;
	        private byte reserved2;
	        private int reserved3; // ushort in C# is an unsigned 16-bit integer; int in Java can represent it correctly
	        private short r_maintype;
	        private short sr_offset1;
	        private byte sr_type1;
	        private short sr_offset2;
	        private byte sr_type2;
	        private short sr_offset3;
	        private byte sr_type3;
	        private short sr_offset4;
	        private byte sr_type4;
	        private short sr_offset5;
	        private byte sr_type5;
	        private short sr_offset6;
	        private byte sr_type6;
	        private short sr_offset7;
	        private byte sr_type7;
	        private short sr_offset8;
	        private byte sr_type8;

	        // Getters and setters for all fields
	        public short getR_len() {
	            return r_len;
	        }

	        public void setR_len(short r_len) {
	            this.r_len = r_len;
	        }

	        public byte getR_nbr() {
	            return r_nbr;
	        }

	        public void setR_nbr(byte r_nbr) {
	            this.r_nbr = r_nbr;
	        }

	        public byte getR_dri_level() {
	            return r_dri_level;
	        }

	        public void setR_dri_level(byte r_dri_level) {
	            this.r_dri_level = r_dri_level;
	        }

	        public int getPlug_id() {
	            return plug_id;
	        }

	        public void setPlug_id(int plug_id) {
	            this.plug_id = plug_id;
	        }

	        public long getR_time() {
	            return r_time;
	        }

	        public void setR_time(long r_time) {
	            this.r_time = r_time;
	        }

	        public byte getReserved1() {
	            return reserved1;
	        }

	        public void setReserved1(byte reserved1) {
	            this.reserved1 = reserved1;
	        }

	        public byte getReserved2() {
	            return reserved2;
	        }

	        public void setReserved2(byte reserved2) {
	            this.reserved2 = reserved2;
	        }

	        public int getReserved3() {
	            return reserved3;
	        }

	        public void setReserved3(int reserved3) {
	            this.reserved3 = reserved3;
	        }

	        public short getR_maintype() {
	            return r_maintype;
	        }

	        public void setR_maintype(short r_maintype) {
	            this.r_maintype = r_maintype;
	        }

	        public short getSr_offset1() {
	            return sr_offset1;
	        }

	        public void setSr_offset1(short sr_offset1) {
	            this.sr_offset1 = sr_offset1;
	        }

	        public byte getSr_type1() {
	            return sr_type1;
	        }

	        public void setSr_type1(byte sr_type1) {
	            this.sr_type1 = sr_type1;
	        }

	        short getSr_offset2() {
	            return sr_offset2;
	        }

	        public void setSr_offset2(short sr_offset2) {
	            this.sr_offset2 = sr_offset2;
	        }

	        public byte getSr_type2() {
	            return sr_type2;
	        }

	        public void setSr_type2(byte sr_type2) {
	            this.sr_type2 = sr_type2;
	        }

	        public short getSr_offset3() {
	            return sr_offset3;
	        }

	        public void setSr_offset3(short sr_offset3) {
	            this.sr_offset3 = sr_offset3;
	        }

	        public byte getSr_type3() {
	            return sr_type3;
	        }

	        public void setSr_type3(byte sr_type3) {
	            this.sr_type3 = sr_type3;
	        }

	        public short getSr_offset4() {
	            return sr_offset4;
	        }

	        public void setSr_offset4(short sr_offset4) {
	            this.sr_offset4 = sr_offset4;
	        }

	        public byte getSr_type4() {
	            return sr_type4;
	        }

	        public void setSr_type4(byte sr_type4) {
	            this.sr_type4 = sr_type4;
	        }

	        public short getSr_offset5() {
	            return sr_offset5;
	        }

	        public void setSr_offset5(short sr_offset5) {
	            this.sr_offset5 = sr_offset5;
	        }

	        public byte getSr_type5() {
	            return sr_type5;
	        }

	        public void setSr_type5(byte sr_type5) {
	            this.sr_type5 = sr_type5;
	        }

	        public short getSr_offset6() {
	            return sr_offset6;
	        }

	        public void setSr_offset6(short sr_offset6) {
	            this.sr_offset6 = sr_offset6;
	        }

	        public byte getSr_type6() {
	            return sr_type6;
	        }

	        public void setSr_type6(byte sr_type6) {
	            this.sr_type6 = sr_type6;
	        }

	        public short getSr_offset7() {
	            return sr_offset7;
	        }

	        public void setSr_offset7(short sr_offset7) {
	            this.sr_offset7 = sr_offset7;
	        }

	        public byte getSr_type7() {
	            return sr_type7;
	        }

	        public void setSr_type7(byte sr_type7) {
	            this.sr_type7 = sr_type7;
	        }

	        short getSr_offset8() {
	            return sr_offset8;
	        }

	        public void setSr_offset8(short sr_offset8) {
	            this.sr_offset8 = sr_offset8;
	        }

	        public byte getSr_type8() {
	            return sr_type8;
	        }

	        public void setSr_type8(byte sr_type8) {
	            this.sr_type8 = sr_type8;
	        }
	    }


	      class DriPhdb {
	    	    private long time; // uint in C# is an unsigned 32-bit integer; long in Java can represent it correctly
	    	    private BasicPhdbType basic = new BasicPhdbType();
	    	     Ext1Phdb ext1 = new Ext1Phdb();
	    	     Ext2Phdb ext2 = new Ext2Phdb();
	    	     Ext3Phdb ext3 = new Ext3Phdb();
	    	    private byte marker;
	    	    private byte reserved;
	    	    private int cl_drilvl_subt; // ushort in C# is an unsigned 16-bit integer; int in Java can represent it correctly

	    	    // Getters and setters
	    	    public long getTime() {
	    	        return time;
	    	    }

	    	    public void setTime(long time) {
	    	        this.time = time;
	    	    }

	    	    public BasicPhdbType getBasic() {
	    	        return basic;
	    	    }

	    	    public void setBasic(BasicPhdbType basic) {
	    	        this.basic = basic;
	    	    }

	    	   


	    	    public byte getMarker() {
	    	        return marker;
	    	    }

	    	    public void setMarker(byte marker) {
	    	        this.marker = marker;
	    	    }

	    	    public byte getReserved() {
	    	        return reserved;
	    	    }

	    	    public void setReserved(byte reserved) {
	    	        this.reserved = reserved;
	    	    }

	    	    public int getCl_drilvl_subt() {
	    	        return cl_drilvl_subt;
	    	    }

	    	    public void setCl_drilvl_subt(int cl_drilvl_subt) {
	    	        this.cl_drilvl_subt = cl_drilvl_subt;
	    	    }

	    	    // Nested classes representing the types used within dri_phdb
//	    	    public static class BasicPhdbType {
//	    	        // Define fields and methods for basic_phdb_type
//	    	    }

	    	 
	    	}
	      
	       class Ext1Phdb
	      {
	          public arrh_ecg_group ecg = new arrh_ecg_group();
	          public ecg_12_group ecg12 = new ecg_12_group();
	         
	          public byte[] reserved = new byte[192];
	          
	      }
	       
	        class Ext2Phdb
	       {
	           public nmt2_group nmt2 = new nmt2_group();
	           public eeg_group eeg = new eeg_group();
	           public eeg_bis_group eeg_bis = new eeg_bis_group();
	           public entropy_group ent = new entropy_group();
	          
	           public byte[] reserved1 = new byte[58];
	           public eeg2_group eeg2 = new eeg2_group();
	          
	           public byte[] reserved = new byte[41];
	           
	       }
	        
	         class Ext3Phdb
	        {
	            public gasex_group gasex = new gasex_group();
	            public flow_vol_group2 flow_vol2 = new flow_vol_group2();
	            public bal_gas_group bal = new bal_gas_group();
	            public tono_group tono = new tono_group();
	            public aa2_group aa2 = new aa2_group();
	         
	            public byte[] reserved = new byte[154];
	            
	        }    
	          class gasex_group
	        {
	            public group_hdr_type hdr = new group_hdr_type();
	            public short vo2;
	            public short vco2;
	            public short ee;
	            public short rq;
	        }
	          
	           class tono_group
	          {
	              public group_hdr_type hdr = new group_hdr_type();
	              public short prco2;
	              public short pr_et;
	              public short pr_pa;
	              public short pa_delay;
	              public short phi;
	              public short phi_delay;
	              public short amb_press;
	              public short cpma;
	          };

	         
	           class aa2_group
	          {
	              public group_hdr_type hdr = new group_hdr_type();
	              public short mac_age_sum;
	          
	              public byte[] reserved = new byte[16];
	              
	          }
	          
	           class flow_vol_group2
	          {
	              public group_hdr_type hdr = new group_hdr_type();
	              public short ipeep;
	              public short pmean;
	              public short raw;
	              public short mv_insp;
	              public short epeep;
	              public short mv_spont;
	              public short ie_ratio;
	              public short insp_time;
	              public short exp_time;
	              public short static_compliance;
	              public short static_pplat;
	              public short static_peepe;
	              public short static_peepi;
	             
	              public short[] reserved = new short[7];
	              
	          }
	           
	            class bal_gas_group
	           {
	               public group_hdr_type hdr = new group_hdr_type();
	               public short et;
	               public short fi;
	           }
	         class nmt2_group
	        {
	            public group_hdr_type hdr = new group_hdr_type();
	            public short reserved;
	            public short nmt_t1;
	            public short nmt_t2;
	            public short nmt_t3;
	            public short nmt_t4;
	            public short nmt_resv1;
	            public short nmt_resv2;
	            public short nmt_resv3;
	            public short nmt_resv4;
	        }
	         
	          class eeg_bis_group
	         {
	             public group_hdr_type hdr = new group_hdr_type();
	             public short bis;
	             public short sqi_val;
	             public short emg_val;
	             public short sr_val;
	             public short reserved;
	         }
	          
	           class entropy_group
	          {
	              public group_hdr_type hdr = new group_hdr_type();
	              public short eeg_ent;
	              public short emg_ent;
	              public short bsr_ent;
	          
	              public short[] reserved = new short[8];
	              
	          }
	          class eeg_group
	         {
	             public group_hdr_type hdr = new group_hdr_type();
	             public short femg;
	             public eeg_channel eeg1;
	             public eeg_channel eeg2;
	             public eeg_channel eeg3;
	             public eeg_channel eeg4;
	          }
	           class eeg_channel
	          {
	              public short ampl;
	              public short sef;
	              public short mf;
	              public short delta_proc;
	              public short theta_proc;
	              public short alpha_proc;
	              public short beta_proc;
	              public short bsr;
	          }
	          
	           class eeg2_group
	          {
	              public group_hdr_type hdr = new group_hdr_type();
	              public byte common_reference;
	              public byte montage_label_ch_1_m;
	              public byte montage_label_ch_1_p;
	              public byte montage_label_ch_2_m;
	              public byte montage_label_ch_2_p;
	              public byte montage_label_ch_3_m;
	              public byte montage_label_ch_3_p;
	              public byte montage_label_ch_4_m;
	              public byte montage_label_ch_4_p;
	         
	              public short[] reserved = new short[8];
	              
	          }
	        class arrh_ecg_group
	       {
	           public group_hdr_type hdr = new group_hdr_type();
	           public short hr;
	           public short rr_time;
	           public short pvc;
	           public int arrh_reserved;
	          
	           public short[] reserved = new short[16];
	           
	       }
	        class ecg_12_group
	       {
	           public group_hdr_type hdr = new group_hdr_type();
	           public short stI;
	           public short stII;
	           public short stIII;
	           public short stAVL;
	           public short stAVR;
	           public short stAVF;
	           public short stV1;
	           public short stV2;
	           public short stV3;
	           public short stV4;
	           public short stV5;
	           public short stV6;
	       }
	       class BasicPhdbType {
	    	    // uint time;
	    	    public EcgGroupType ecg = new EcgGroupType();
	    	    // public PGroupType[] p1234 = new PGroupType[4];
	    	    public p_group_type p1 = new p_group_type();
	    	    public p_group_type p2 = new p_group_type();
	    	    public p_group_type p3 = new p_group_type();
	    	    public p_group_type p4 = new p_group_type();
	    	    public nibp_group_type nibp;
	    	    // public TGroupType[] t = new TGroupType[4];
	    	    public t_group_type t1 = new t_group_type();
	    	    public t_group_type t2 = new t_group_type();
	    	    public t_group_type t3 = new t_group_type();
	    	    public t_group_type t4 = new t_group_type();
	    	    public SpO2GroupType SpO2 = new SpO2GroupType();
	    	    public co2_group_type co2 = new co2_group_type();
	    	    public o2_group_type o2 = new o2_group_type();
				
				  public N2OGroupType n2o = new N2OGroupType(); 
				  public AAGroupType aa = new  AAGroupType();
				  public FlowVolGroupType flow_vol = new FlowVolGroupType();
				  public CoWedgeGroupType co_wedge = new CoWedgeGroupType();
				  public NmtGroup  nmt = new NmtGroup();
				  public EcgExtraGroup ecg_extra = new EcgExtraGroup();
				  public Svo2Group svo2 = new Svo2Group(); 
				  public p_group_type[] p56 = new  p_group_type[2]; 
				  public p_group_type p5 = new p_group_type();
				  public p_group_type p6  = new p_group_type();
				 
	    	    public byte[] reserved = new byte[2]; // Adjust the size if necessary

	    	    // Add constructors, getters, setters, and other methods if needed
	    	}

	        class EcgGroupType {
	    	    public group_hdr_type hdr = new group_hdr_type();
	    	    public short hr;
	    	    public short getHr() {
					return hr;
				}
				public void setHr(short hr) {
					this.hr = hr;
				}
				public short getSt1() {
					return st1;
				}
				public void setSt1(short st1) {
					this.st1 = st1;
				}
				public short getSt2() {
					return st2;
				}
				public void setSt2(short st2) {
					this.st2 = st2;
				}
				public short getSt3() {
					return st3;
				}
				public void setSt3(short st3) {
					this.st3 = st3;
				}
				public short getImpRr() {
					return impRr;
				}
				public void setImpRr(short impRr) {
					this.impRr = impRr;
				}
				public short st1;
	    	    public short st2;
	    	    public short st3;
	    	    public short impRr;

	    	    // Add constructors, getters, setters, and other methods if needed
	    	}
	        
	         class p_group_type
	        {
	            public group_hdr_type hdr = new group_hdr_type();
	            public short sys;
	            public short dia;
	            public short mean;
	            public short hr;
	        }
	         
	          class nibp_group_type
	         {
	             public group_hdr_type hdr = new group_hdr_type();
	             public short sys;
	             public short dia;
	             public short mean;
	             public short hr;
	         }
	           class t_group_type
	          {
	              public group_hdr_type hdr = new group_hdr_type();
	              public short temp;
	          }
	           class group_hdr_type {
	        	    public long statusBits; // Using long for uint equivalent
	        	    public int labelInfo;   // Using int for ushort equivalent

	        	    // Add constructors, getters, setters, and other methods if needed
	        	}
	           
	            class SpO2GroupType {
	        	    public group_hdr_type hdr = new group_hdr_type();
	        	    public short SpO2;
	        	    public short pr;
	        	    public short irAmp;
	        	    public short svo2;

	        	    // Add constructors, getters, setters, and other methods if needed
	        	}
	             class co2_group_type
	            {
	                public group_hdr_type hdr = new group_hdr_type();
	                public short et;
	                public short fi;
	                public short rr;
	                public short amb_press;
	            }
	         
	             
	             
	              class o2_group_type
	             {
	                 public group_hdr_type hdr = new group_hdr_type();
	                 public short et;
	                 public short fi;
	             }
	               class N2OGroupType {
	            	    public group_hdr_type hdr = new group_hdr_type();
	            	    public short et;
	            	    public short fi;
	            	}

	                class AAGroupType {
	            	    public group_hdr_type hdr = new group_hdr_type();
	            	    public short et;
	            	    public short fi;
	            	    public short macSum;
	            	}

	                
	                 class FlowVolGroupType {
	                    public group_hdr_type hdr = new group_hdr_type();
	                    public short rr;
	                    public short ppeak;
	                    public short peep;
	                    public short pplat;
	                    public short tvInsp;
	                    public short tvExp;
	                    public short compliance;
	                    public short mvExp;
	                }
	                 
	                  class CoWedgeGroupType {
	                	    public group_hdr_type hdr = new group_hdr_type();
	                	    public short co;
	                	    public short bloodTemp;
	                	    public short rightEf;
	                	    public short pcwp;
	                	}
	                  
	                   class NmtGroup {
	                	    public group_hdr_type hdr = new group_hdr_type();
	                	    public short t1;
	                	    public short tratio;
	                	    public short ptc;
	                	}
	                   
	                    class EcgExtraGroup {
	                	    public short hrEcg;
	                	    public short hrMax;
	                	    public short hrMin;
	                	}
	                    
	                     class Svo2Group {
	                        public group_hdr_type hdr = new group_hdr_type();
	                        public short svo2;
	                    }

							 class DataConstants
							{
								// Data validity macros
							    public static final int DATA_INVALID_LIMIT = -32001; // limit for special invalid data values
							    public static final int DATA_INVALID = -32767; // there is no valid data
							    public static final int DATA_NOT_UPDATED = -32766; // data is not updated
							    public static final int DATA_DISCONT = -32765; // data discontinuity (calibration ...)
							    public static final int DATA_UNDER_RANGE = -32764; // data exceeds lower valid limit
							    public static final int DATA_OVER_RANGE = -32763; // data exceeds upper valid limit
							    public static final int DATA_NOT_CALIBRATED = -32762; // data is not calibrated
						
							    // General definitions
							    // Asynchronous interface specific constants
							    public static final byte FRAMECHAR = 0x7E;
							    public static final byte CTRLCHAR = 0x7D;
							    public static final byte BIT5 = 0x7C;
							    public static final byte BIT5COMPL = 0x5F;
						
							    // Datex Record Interface data structure definitions
							    public static final int DRI_MAX_SUBRECS = 8; // # of subrecords in a packet
							    public static final int DRI_MAX_PHDBRECS = 5; // # of phys.db records in a packet
						
							    // data packet maintypes
							    public static final short DRI_MT_PHDB = 0; // Physiological data and related transmission requests.
							    public static final short DRI_MT_WAVE = 1; // Waveform data and related transmission requests.
							    public static final short DRI_MT_ALARM = 4; // Alarm data and related transmission requests.
						
							    // data packet subtypes
							    public static final byte DRI_PH_DISPL = 1;
							    public static final byte DRI_PH_10S_TREND = 2;
							    public static final byte DRI_PH_60S_TREND = 3;
						
							    public static final int DRI_PHDBCL_REQ_BASIC_MASK = 0x0000; // Enable sending of Basic physiological data class
							    public static final int DRI_PHDBCL_DENY_BASIC_MASK = 0x0001; // Disable sending of Basic physiological data class
							    public static final int DRI_PHDBCL_REQ_EXT1_MASK = 0x0002; // Enable sending of Ext1 physiological data class
							    public static final int DRI_PHDBCL_REQ_EXT2_MASK = 0x0004; // Enable sending of Ext2 physiological data class
							    public static final int DRI_PHDBCL_REQ_EXT3_MASK = 0x0008; // Enable sending of Ext3 physiological data class
						
							    // Datex Record Interface level types
							    public static final byte DRI_LEVEL_95 = 0x02;
							    public static final byte DRI_LEVEL_97 = 0x03;
							    public static final byte DRI_LEVEL_98 = 0x04;
							    public static final byte DRI_LEVEL_99 = 0x05;
							    public static final byte DRI_LEVEL_2000 = 0x06;
							    public static final byte DRI_LEVEL_2001 = 0x07;
							    public static final byte DRI_LEVEL_2003 = 0x08;
							    public static final byte DRI_LEVEL_2005 = 0x09;
							
						
						
						
						}

						public class DatexRecordType {
						    DatexHdrType hdr = new DatexHdrType();
						    private byte[] data = new byte[1450];
						
						    // Getters and Setters for hdr
						    public DatexHdrType getHdr() {
						        return hdr;
						    }
						
						    public void setHdr(DatexHdrType hdr) {
						        this.hdr = hdr;
						    }
						
						    // Getters and Setters for data
						    public byte[] getData() {
						        return data;
						    }
						
						    public void setData(byte[] data) {
						        if (data.length != 1450) {
						            throw new IllegalArgumentException("Data array must be 1450 bytes in length");
						        }
						        this.data = data;
						    }
						}

						   class GroupHdrType {
							    public long statusBits; // Using long for uint equivalent
							    public int labelInfo;   // Using int for ushort equivalent
						
							    // Add constructors, getters, setters, and other methods if needed
							}
	                     
	                    





