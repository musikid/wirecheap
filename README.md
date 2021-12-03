# Wirecheap

## Wireshark, but it's cheaper (and less good).

## Project structure

### src/main/java/com/lu3in033/projet

This is the root of the project is located. The `Main` class is defined here.

### src/main/java/com/lu3in033/projet/layers

Definitions of the layers used in this project (Ethernet, IP, UDP, DNS, DHCP).

### src/main/java/com/lu3in033/projet/parser

This is where the parser is defined. It treats the frames and converts them into a list of bytes.