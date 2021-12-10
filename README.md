# Wirecheap

## Wireshark, but it's cheaper (and less good).

NOTE: The zip release is distributed with binaries for each platform.

## Requirements

- Java 17
- Maven

## Build

```shell
mvn package
```

## Execute

```shell
java -jar target/Wirecheap*.jar FILE
```

## Project structure

### src/main/java/com/lu3in033/projet

This is where the root of the project is located. The `Main` class is defined here.

### src/main/java/com/lu3in033/projet/layers

Definitions of the layers used in this project (Ethernet, IP, UDP, DNS, DHCP). Each layer consume data from
a `ByteBuffer` and returns an instance of the class (by using `.create()`).

### src/main/java/com/lu3in033/projet/parser

This is where the parser is defined. It treats the frames and converts them into a list of bytes.