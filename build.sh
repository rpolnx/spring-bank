CURRENT_VERSION=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec | tr -d " SNAPSHOT" | tr -d " -")

if [ -n "$1" ]; then
  NEXT_VERSION=$1
else
  NEXT_VERSION=$(node -p "const temp = \"$CURRENT_VERSION\".split(\".\");temp[2]++;temp.join(\".\")")
fi

mvn versions:set -DnewVersion="$NEXT_VERSION-SNAPSHOT"
mvn versions:commit

#mvn clean install

cp Dockerfile customer
cp logback-spring.xml customer
cp wait-for-it.sh customer
cp Dockerfile service
cp logback-spring.xml service
cp wait-for-it.sh service
cp Dockerfile account
cp logback-spring.xml account
cp wait-for-it.sh account

docker build -t rpolnx/customer:$CURRENT_VERSION customer
docker build -t rpolnx/service:$CURRENT_VERSION service
docker build -t rpolnx/account:$CURRENT_VERSION account

rm */Dockerfile
rm */logback-spring.xml
rm */wait-for-it.sh

sed -i 's/\b[0-9]\{1,3\}.\b[0-9]\{1,3\}.\b[0-9]\{1,3\}/'$CURRENT_VERSION'/g' .env

echo "Processed finalized successfully"