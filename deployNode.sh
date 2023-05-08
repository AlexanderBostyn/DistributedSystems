if [ $1 -eq 0 ]; then
    echo "No arguments given"
    exit 1
fi
cd Node/
git reset --hard
git pull origin test
chmod +x ../deployNode.sh
mvn clean install
java -jar target/Node-0.0.1-SNAPSHOT.jar $1

