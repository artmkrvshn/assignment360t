mvn clean install

echo "Start in same PID or separated PIDs?: 1 - same, 2 - separated"
read answer

if [[ "$answer" == "1" || "$answer" == "same" ]]; then
    echo "Starting in same PID"
    java -cp target/test360t-1.0-SNAPSHOT.jar org.task.model.PlayerServer &
    java -cp target/test360t-1.0-SNAPSHOT.jar org.task.model.Player &
    wait
elif [[ "$answer" == "2" || "$answer" == "separated" ]]; then
    echo "Starting in separated PIDs"
    java -cp target/test360t-1.0-SNAPSHOT.jar org.task.Main

else
    echo "Invalid input. Please enter '1' or '2'."
fi