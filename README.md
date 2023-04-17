# wherearetherocas

a personal finances management application

## How to build

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install -s settings.xml

## How to run

To run the application locally run in the project root directory the following command with Maven 3:

    mvn jetty:run
or

    mvn clean install -s settings.xml jetty:run

if default port (8080) is being used you can do the following

    mvn clean install -s settings.xml jetty:run -Djetty.http.port=9999