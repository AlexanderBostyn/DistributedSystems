#Script to pull latest code and to deploy the naming server
cd Naming-server/
git pull origin test
mvn clean install
java -jar target/Naming-server-0.0.1-SNAPSHOT.jar
#Your awesome titleclear data.json
#mvn clean install
#mvn deploy