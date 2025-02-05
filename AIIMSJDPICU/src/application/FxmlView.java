/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package application;

import java.util.ResourceBundle;

public enum FxmlView {

    MAIN {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("main.app.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/NewDashboard.fxml";
        }
    }, SEARCH_PATIENT {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("main.app.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/searchPatient.fxml";
        }
    },
    CREATE_PATIENT {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("main.app.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/createPatient.fxml";
        }
    },
    LOGIN {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/Login.fxml";
        }
    }, VIEW_ALL_CASES{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/viewAllCase.fxml";
        }
    },
    ANESTHESIA_DETAILS{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/AnaethesiaMultiTab.fxml";
        }
    },
    EVENTS_LOG{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/Events.fxml";
        }
    },
    SHIFT_OUT{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/ShiftOut.fxml";
        }
    },
    MANAGE_DEVICES{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/ManageAvailableDevice.fxml";
        }
    },
    VASCULAR_CANNULATIONS{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/VascularCannulations.fxml";
        }
    },
    CREATE_CASE{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/createCase.fxml";
        }
    },
    HISTORICAL_DATA{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/HistoricalData.fxml";
        }
    },
    GRID_HISTORICAL{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/GridsHistoricalView.fxml";
        }
    },
    DEVICES{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/Devices.fxml";
        }
    }, FLUIDS{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/Fluids.fxml";
        }
    }, ASSIGN_INFUSION_PUMP{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/AssignInfusionPump.fxml";
        }
    }, DIALOG_BOX{
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/dialogueBox.fxml";
        }
    }, MANAGE_DEVICE_PARAMS{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/ManageDeviceParams.fxml";
        }
    }, CONFIGURE_DEVICE{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/ConfigureDevice.fxml";
        }
    }, MEDICATION_HISTORY{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/medicationHistory.fxml";
        }
    }, MEDICATION{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/Medication.fxml";
        }
    }, OUTPUT{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/output.fxml";
        }
    }, CALCULATE_ALDERATE_SCORE{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/CalculateAldreteScore.fxml";
        }
    }, DASHBOARD_EVENT{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/DashboardEvent.fxml";
        }
    }, PATIENT_MONITOR{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/PatientMonitor.fxml";
        }
    }, DASHBOARD_OUTPUT{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/DashboardOutput.fxml";
        }
    }, DASHBOARD_FLUID_SEARCH{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/DashboardFluidSearch.fxml";
        }
    }, DASHBOARD_FLUID_START{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/DashboardFluidStart.fxml";
        }
    }, DASHBOARD_ADD_NEW_TEST{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/DashboardAddNewTest.fxml";
        }
    }, DASHBOARD_TEST{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/DashboardTest.fxml";
        }
    }, SEARCH_MEDICINE{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/SearchMedicine.fxml";
        }
    }, MEDICATION_INFUSION{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/MedicationInfusion.fxml";
        }
    }, STOP_INFUSION{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/StopInfusion.fxml";
        }
    }, DASHBOARD_ANESTHESIA{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/DashboardAnaethesia.fxml";
        }
    }, GET_SNAPSHOT{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/GetSnapShot.fxml";
        }
    }, SEARCH_CASE{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/SearchCase.fxml";
        }
    },
    SPLASH_LOADER {
        @Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/SplashScreenFinal.fxml";
        }
    }, CPB_DETAILS{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/CPBDetails.fxml";
        }
    }, ECHO_DETAILS{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/EchoDetails.fxml";
        }
    }, ICU_PLAN_DETAILS{
    	@Override
        String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        String getFxmlFile() {
            return "/View/ICUPlan.fxml";
        }
    };

    abstract String getTitle();
    abstract String getFxmlFile();

    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }

}
