## About Application:

This is spring boot service contains single end point /authorizeTransactions.

It will accept multiple files and reads them one by one, if they are of type csv/xml, otherwise it ignores.

After hitting the end point statement ( statement.csv ) will be generated in current directory/output

## Running Information:

Clone the given URL to your local repo.

Build the application using maven and create the jar.

Go to the target folder and get the customerStatementProcessor-0.0.1-SNAPSHOT.jar

Run the cmd java -jar customerStatementProcessor-0.0.1-SNAPSHOT.jar to start the application
