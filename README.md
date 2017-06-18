# Cas2Mysql
A simple example of cas consumer that stores annotations into a database.

This project requires maven to build. For more information on the UIMA setup, please read these guides http://uima.apache.org/d/uimaj-current/index.html 

Main descriptor uses mysql as database to store the annotations. Check the descriptors parameters for more information.
Tests use a slightly different descriptor, it uses a sqlite database to run, so no need to prior setup to run it.

`mvn package` will also generate the PEAR file that can be used in a pipeline

Databases configurations are stored in the xml descriptor, you should create the proper database and tables or modify the parameters accordingly.

Bear in mind that the table used in this project is a generic one and does not contain features that aren't default.




