#Script to pull latest code and to deploy the naming server
cd Naming-server/
git reset --hard
git pull origin test
mvn clean install
rm src/main/resources/data.json
touch src/main/resources/data.json
echo '{}' > src/main/resources/data.json
java -jar target/Naming-server-0.0.1-SNAPSHOT.jar
#Your awesome titleclear data.json
#mvn clean install
#mvn deploy