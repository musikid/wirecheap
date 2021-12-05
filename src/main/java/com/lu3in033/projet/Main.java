package com.lu3in033.projet;

import com.lu3in033.projet.layers.dhcp.Dhcp;
import com.lu3in033.projet.layers.ethernet.EtherTypes;
import com.lu3in033.projet.layers.ethernet.Ethernet;
import com.lu3in033.projet.layers.ipv4.Ipv4;
import com.lu3in033.projet.layers.ipv4.NextHeaderProtocols;
import com.lu3in033.projet.layers.udp.Udp;
import com.lu3in033.projet.parser.Frame;
import com.lu3in033.projet.parser.Parser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: wirecheap [file]");
            return;
        }

        Path p = Path.of(args[0]);
        Parser parser = new Parser();
        try {
            List<Frame> frames = parser.parse(Files.readString(p));
            for (Frame frame : frames) {
                System.out.printf("Frame #%d%n%n", frame.id);
                Ethernet eth = Ethernet.create(frame.buffer);
                System.out.println(eth);
                if (eth.type.value() != EtherTypes.IPv4.value()) {
                    System.out.println("Payload: " + eth.payload());
                    return;
                }

                Ipv4 ip = Ipv4.create(frame.buffer);
                System.out.println(ip);
                if (ip.nextHeaderProtocol.value != NextHeaderProtocols.UDP.value()) {
                    System.out.println("Payload: " + ip.payload());
                    return;
                }

                Udp udp = Udp.create(frame.buffer);
                System.out.println(udp);
                if (udp.destPort == 67 || udp.destPort == 68) {
                    Dhcp dhcp = Dhcp.create(frame.buffer);
                    System.out.println(dhcp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
