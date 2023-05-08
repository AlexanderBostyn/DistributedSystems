cd /Node
git pull origin test
mvn clean install
java -jar target/Node-0.0.1-SNAPSHOT.jar $1