# SERVER CLIENT CHAT APPLICATION

## Requirements
  1. java jdk 11
  2. sqlite database
  3. java-json driver (present in libs package)
  4. sqlite jdbc driver (present in libs package)

## To run the application:  
**Step 1:** Clone the project and open it in terminal  
```
$ cd ServerClientChatApplication/src

```
**Step 2:**   Export the drivers
```
export CLASSPATH=~<path>/sqlite-jdbc-3.30.1.jar:$CLASSPATH
export CLASSPATH=~<path>/json.jar:$CLASSPATH
```
**Step 3:** Compile the classes  
```
add the path of your database in Connect class in database package
javac -d ../out Main/*.java Database/*.java Communication/*.java
```
**Step 4:** start the server  
```
cd ../out 
java Main.ServerService <serverport>
```
**Step 5:** start the clients  
```
cd ../out 
java Main.ClientService <hostname> <serverport> <listenport> <clientname>
```
