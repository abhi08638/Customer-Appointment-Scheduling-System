# Customer-Appointment-Scheduling-System--v0.01
This is a material design based appointment management system created in JavaFX 8 with full localization support.

The goal is to provide the users with a beautiful UI while working. I am using a custom made material blue theme to mimic material design in JavaFX. 

To add support for more languages, please create a properties file with all the translations. There is support for and automatic parsing of 12h to 24h time, as well as conversion for appointment times created in different time zones. I store all my date time values in UTC in my database and then convert back and forth to maintain consistency using Java 8's java.time package.
