# Wirecheap

## Wireshark, mais moins cher (et moins bon).

## Structure du projet

### src/main/java/com/lu3in033/projet

C'est ici que se trouve la racine. La classe `Main` est définie ici.

### src/main/java/com/lu3in033/projet/layers

Définitions des couches utilisées dans ce projet (Ethernet, IP, UDP, DNS, DHCP).

### src/main/java/com/lu3in033/projet/parser

C'est ici que l'analyseur syntaxique est défini. Il traite les trames et les convertit en une liste d'octets.