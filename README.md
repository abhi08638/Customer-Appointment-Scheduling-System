# Customer-Appointment-Scheduling-System--v1.00
Author: Abhishek Gandhi
Application: Customer Appointment Scheduling System
This is a material design based appointment management system created in JavaFX 8 with full localization support.

This application has been designed to work with the provided database structure.
The only changes made were to add auto-increments to the primary keys in the tables.

Login username:test password:test
Login username:test2 password:test2

Bypassing login in case db is down
Login username:uidemo password:test 

Localization
---------------------------------
-This application fully supports Localization for English and French
-To set the French language, open ApplicationConfig.java and uncomment this line "//Locale.setDefault(Locale.FRANCE);"
-Date and Times are converted automatically depending on the time zone your computer is set to.

Getting/Setting Data
---------------------------------
-DBConn.java is the layer that connects to the database to retrieve and save data.
-Once the data is received, it is put into a dataobject like Appointment.java
-After the dataobject is created, the data is then transferred into a Data Transfer Object (AppointmentDto.java)
	This allows to the application to transform the data types of any of the fields for use in the front end
-All the mappings from DO to DTO and vice versa occur in DataMapper.java

Front End - UI
---------------------------------
-The application is themed to mimic Material Design
-All the styles are located in app.css

Front End - Controllers
---------------------------------
-When saving data, the controllers call the Validator class to make sure the fields in the UI are not empty

Lambda Uses
---------------------------------
-Lambda expressions are used where they make sense. Since the application does not require user input in alert dialogs, they are not used there.
-In the Main controller, Lambda Filters and streams are used to get data from a list. This allows for efficient searching for the data grids.
	-When initializing data for the grids, Lambda is used to set the cell factory (appCustomer.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(getCustomerName(cellData.getValue().getCustomerId())));)
-For focusing on a field when a page is used, something like "Platform.runLater(() -> userNameText.requestFocus());" is used.	

DateTime Manipulation
---------------------------------
-The application uses Instant datatype to convert all dates and times
-DataObjects store dates in UTC Instant
-DataTransferObjects store the string representation of the local time zone for the user in the date and time format specified in the properties file for the language
-All Date Time conversions are done using the DateUtils.java class
-Getting data
	DateTime UTC(DB)->LocalDateTime UTC->Instant UTC(DO)->
-Saving data
	String user local time(DTO)->Instant UTC(DO)->LocalDateTime UTC->DateTime UTC(DB)	
	
Reports
---------------------------------
-Reports are generated using the ReportGenerator.java class 
-All the data is placed into the ReportSkeleton.java data object which just takes an arraylist of strings which are essentially rows of data
-Consultant Report outputs the schedules of all the users to a text file
-Type Report outputs the amount of different appointment types by month to a text file
-Customer Appointment Report outputs how many times every customer had an appointment by month to a text file
-Login log file appends each successful user login

Calender  
---------------------------------
-The calendar only shows appointments in which the end date and time are greater than the current time for that specific user. 
