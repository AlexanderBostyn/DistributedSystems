if [ $# -eq 0 ]; then
    echo "No arguments given"
    exit 1
fi
cd Node/
git reset --hard origin/test2
git pull origin test2
chmod +x ../deployNode.sh
rm -r src/main/resources/local/*
rm -r src/main/resources/replicated/*
mkdir src/main/resources/local
mkdir src/main/resources/replicated
mvn clean install
touch src/main/resources/local/$1.file
java -jar target/Node-0.0.1-SNAPSHOT.jar $1

