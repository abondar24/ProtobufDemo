# Protobuf Demo

Small demo showing usage of Google's Protocol Buffers 
serialization mechanism in combination with ZeroMQ.


More info about [ProtocolBuffer]() and [ZeroMQ]().

## Build and run
```yaml
mvn clean install

java -jar targer/protobufdemo.jar
```

In order to add custom message 
```yaml
protoc -I=<project_dir> --java_out=<project_dir>/<sources_dir> <project_dir>/<proto_file_dir>/file.proto
```
