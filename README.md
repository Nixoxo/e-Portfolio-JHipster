# e-Portfolio-JHipster

Dieses Repository beschreibt mein e-Portfolio über JHipster in Softwareenginering.

##Requirements
1. Java 8
2. Maven / Gradle
3. NodeJS
4. Yeoman
5. Bower
6. Gulp
7. JHipster


##Installation von JHipster
1. [NodeJS](https://nodejs.org/en/)
2. Yeoman: ```npm install -g yo```
2. Bower: ```npm install -g bower```
3. Gulp: ```npm install -g gulp```
4. JHipster: ```npm install -g generator-jhipster```


##Tutorial
1. Erstelle einen Ordner und führe in diesem Verzeichnis den folgenden Befehl  aus: ```yo jhipster```
	* 1/16 Monolithic application
	* 6/16 SQL
	* 7/16 MySQL
	* 8/16 H2 with disk-based persistence
	* 13/16 Gradle
	* 16/16 Cucumber
2. Erstelle die Entities ```Owner``` und ```Car``` mit dem [JDL Studio](http://jhipster.github.io/jdl-studio/). Wie die einzelen Datentypen spezifiert werden können, kann aus [hier](http://jhipster.github.io/jdl/) nachgelassen werden.
3. Der ```Owner``` verfügt über folgende Attribute:
	* Firstname
	* Lastname
	* Birthyear
3. Das ```Car``` verfügt über folgende Attribute:
	* Model
	* Company
	* DateOfProduction
4. Ein ```Car``` verfügt genau über einen ```Owner```. Ein ```Owner``` kann über mehrere ```Cars``` verfügen.
4. Importiere die erstellen Entitäten in JHipster mit dem folgendem Befehl ```yo jhipster:import-jdl filename.jh```
5. Führe die Tests aus.
6. Starte die Anwendung und lege einen ```Owner``` mit einem zugehörigen ```Car``` an.