cd $(dirname $0)
cd ..
mvn clean test javadoc:javadoc -Dcucumber.options="--tags @hello,@bonjour,@blog,@playToLogoGame,@jouerAuJeuDesLogos" -PscenarioInitiator,javadoc,unit-tests -Dmaven.test.failure.ignore=true

echo "NORA-UI is ready"

a = `travis logs`
echo $a

rm -rf target

exit