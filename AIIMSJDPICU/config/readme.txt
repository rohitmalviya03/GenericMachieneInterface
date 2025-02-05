Release v1.6.0 on 26th june 2018

Release v1.7.0 Includes following Bug Fixes and Features:


1.Loggers are added and exception handling is implemented in all modules
2.Test values are not getting refreshed after update, It works fine if values are changed but doesn't work when we remove the parameter value.
3.Patient report will be generated during Shift out in IntraOp. Now Report can be saved in data base and can be retrieved from data base. The report location is also configurable.
4.Edit an event in event log accepts spaces in the beginning of the event name .
5.Calculate score feature in Shift out, Reset is not working as expected.
6.Edit patient details when a case is loaded the updated details are not being reflected on the top Information Bar.
7.Event Log can add custom events with names already present in the events list, a check should be present to not add a duplicate copy of an event.
8.Edit case after case load is not working as expected. 
9.After case load, When specialty is changed the Event side bar is not getting refreshed.
10.Anesthesia Details issue.
	•Under General Anesthesia >> Ventilation >> Controlled. Enter all the parameters except Airway Pressure. This will not save the data for other parameters and error message appears “Please enter at least one controlled parameter” even if more than one parameter has the data.
	•Regional Anesthesia >> Medication . Add an entry in this section when a row is already present. This will add the new entry but it’ll show 2 copies of the already present row. Also the delete operation in this section throws an exception
	       11.After Creating a new patient and a new case, load the same 
	•Add in time 
	•Go to fluids dropdown in recorded data , 
	•This shows previously added data starting from the in time timestamp till the current time <<NOTE: we have not added any data for this case yet.>>
11.Save Device Data in 1 min and display it in 5 min
12.Implemented capture from device functionality on shift out page

Release v1.7.1 Includes following Bug Fixes and Features:

1.Validations are not working as expected in Shift Out
2.Empty test values are populated as "null" in IntraOp report
3.Event time populated in Event side bar is not correct for a specific time interval(12..00 pm-12.59pm)
4.Edit live infusions on Live and historical grids
5.Remove TTE and TEE from Add New Test list in main Dashboard
6.Validation for Medication dose unit is not working as expected
7.Handover events and Skin Closure events should be added.
8.Event name should allow numbers
9.Cannulations to be updated to handle different type of cannulations.
10.	There should be a section of critical events.
11.PAC application link can be given in Intra-Op
12.Fluid Log, Edit Patient Tabs can be removed
13.	Echo details to be captured (Pre, Intra-Op, Post)
14.	Event time populated in Event side bar is not correct for a specific time interval(12..00 pm-12.59pm)
15.Empty test values are populated as "null" in IntraOp report
16.	Validations are not working as expected in Shift Out
17.	CPB Details window to be added as discussed.(New Screen)
18.Cannulations to be tabbed. Existing one to go in Central and new Tab will be Arterial which includes (Update to existing screen)
19.Update ABG brings up too many pop ups.


Release v1.7.2 Includes following Bug Fixes and Features:

1.CPB Details and Echo details added in Report.
2.Cannulation Details updated in report.
3.Comments added for Events in Report.
4.Multi line comments issue in Report has bee fixed.
5.Report Redesigned
6.Create patient select State validation is not woking as expected
7.Validation in Cannulation save, CPB details save and Echo Details save updated.