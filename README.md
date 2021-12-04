# Wirecheap

## Wireshark, but it's cheaper (and less good).

## Requirements

- Java 17
- Maven

## Build

```shell
mvn package
```

## Execute

```shell
java -jar target/Wirecheap*.jar
```

## Project structure

### src/main/java/com/lu3in033/projet

This is the root of the project is located. The `Main` class is defined here.

### src/main/java/com/lu3in033/projet/layers

Definitions of the layers used in this project (Ethernet, IP, UDP, DNS, DHCP).

### src/main/java/com/lu3in033/projet/parser

This is where the parser is defined. It treats the frames and converts them into a list of bytes.