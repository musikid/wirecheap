package com.lu3in033.projet;

import com.lu3in033.projet.layers.dhcp.Dhcp;
import com.lu3in033.projet.layers.ethernet.EtherTypes;
import com.lu3in033.projet.layers.ethernet.Ethernet;
import com.lu3in033.projet.layers.ipv4.Ipv4;
import com.lu3in033.projet.layers.ipv4.NextHeaderProtocols;
import com.lu3in033.projet.layers.udp.Udp;
import com.lu3in033.projet.parser.Frame;
import com.lu3in033.projet.parser.Parser;
import com.lu3in033.projet.parser.combinators.ParseException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    static int DEFAULT_WIDTH = 10;

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
                    System.out.println("Payload: " + eth.payloadString());
                    continue;
                }

                Ipv4 ip = Ipv4.create(frame.buffer);
                System.out.println(ip);
                if (ip.nextHeaderProtocol.value != NextHeaderProtocols.UDP.value()) {
                    System.out.println("Payload: " + ip.payloadString());
                    continue;
                }

                Udp udp = Udp.create(frame.buffer);
                System.out.println(udp);
                if (udp.destPort == 67 || udp.destPort == 68) {
                    Dhcp dhcp = Dhcp.create(frame.buffer);
                    System.out.println(dhcp);
                } else {
                    System.out.println("Payload: " + udp.payloadString());
                }
            }
        } catch (ParseException e) {
            String line = e.content.lines().skip(e.line - 1).findFirst().get();
            int extractStart = Math.max(0, e.column - DEFAULT_WIDTH);
            int extractEnd = Math.min(line.length(), e.column + DEFAULT_WIDTH);
            String extract = line.substring(extractStart, extractEnd);
            System.err.println(new ErrorReporter(extract, extractStart, e.column, e.line, e.expected));
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }
}
