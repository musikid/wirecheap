package com.lu3in033.projet;

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
import java.util.Objects;

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
            Frame frame = frames.get(0);
            Ethernet eth = Ethernet.create(frame.buffer);
            System.out.println(eth);
            if (eth.type.value() != EtherTypes.IPv4.value()) {
                System.out.println("Payload: " + eth.payload());
                return;
            }

            Ipv4 ip = Ipv4.create(frame.buffer);
            System.out.println(ip);
            if (!Objects.equals(ip.nextHeaderProtocol.name(), NextHeaderProtocols.UDP.name())) {
                System.out.println("Payload: " + ip.payload());
                return;
            }

            Udp udp = Udp.create(frame.buffer);
            System.out.println(udp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
