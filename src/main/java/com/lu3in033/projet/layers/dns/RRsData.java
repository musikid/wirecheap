package com.lu3in033.projet.layers.dns;

import com.lu3in033.projet.layers.NotEnoughBytesException;
import com.lu3in033.projet.layers.ipv4.Ipv4Address;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class RRsData {
    //attributs
    public static int startingPosition;
    //A
    public static short typeA = 0x0001;
    //MX
    public static short typeMX = 0x000F;
    //NS
    public static short typeNS = 0x0002;
    //PTR
    public static short typePTR = 0x000C;
    //SOA
    public static short typeSOA = 0x0006;
    //ANY
    public static short typeAny = 0x00ff;
    //AAA
    public static short typeAAA = 0x001C;
    //CNAME
    public static short typeCNAME = 0x0005;
    //listType
    public static short[] listTab = new short[]{typeA, typeMX, typeNS, typeNS, typeSOA, typeAny};
    public boolean questionSection;
    //Question section
    public String domain; //OK
    public short type;
    public short classe;
    //Other sections
    public int ttl;
    public short rdlenght;
    public Ipv4Address ip;
    public short preference;
    public String exchange;    //OK
    public String nsdName;    //OK
    public String ptrName;    //OK
    public String mName;        //OK
    public String rName;        //OK
    public int serial;
    public int refresh;
    public int retry;
    public int expire;
    public String aaaaAdress;
    public String cName;


    //constructeur
    public RRsData(String domain, short type, short classe, int ttl, short rdlenght, Ipv4Address ip, short preference,
                   String exchange, String nsdName, String ptrName, String mName, String rName,
                   int serial, int refresh, int retry, int expire, String aaaaAdress, String cName, boolean questionSection) {

        this.domain = domain;
        this.type = type;
        this.classe = classe;
        this.ttl = ttl;
        this.rdlenght = rdlenght;
        this.ip = ip;
        this.preference = preference;
        this.exchange = exchange;
        this.nsdName = nsdName;
        this.ptrName = ptrName;
        this.mName = mName;
        this.rName = rName;
        this.serial = serial;
        this.refresh = refresh;
        this.retry = retry;
        this.expire = expire;
        this.aaaaAdress = aaaaAdress;
        this.cName = cName;
        this.questionSection = questionSection;
    }


    public static RRsData create(ByteBuffer bytes, boolean questionSection) {
        //to recover the common values


        String domain = readDomain(bytes, 255);
        short type = bytes.getShort();
        short classe = bytes.getShort();
        String vide = "";


        if (type != typeA && type != typeMX && type != typeNS && type != typePTR && type != typeSOA && type != typeAny
                && type != typeCNAME && type != typeAAA) {
            Dns.stopParsing = true;
            return new RRsData(domain, type, (short) -1, -1, (short) -1, null, (short) -1, vide, vide, vide,
                    vide, vide, -1, -1, -1, -1, vide, vide, questionSection);
        }

        if (questionSection) {
            return new RRsData(domain, type, classe, -1, (short) -1, null, (short) -1, vide, vide, vide,
                    vide, vide, -1, -1, -1, -1, vide, vide, questionSection);

        } else {
            int ttl = bytes.getInt();
            short rdlenght = bytes.getShort();

            if (type == typeA) {
                ByteBuffer rawIP = ByteBuffer.allocate(4);
                bytes.get(rawIP.array());
                Ipv4Address ipv4 = null;
                try {
                    ipv4 = Ipv4Address.create(rawIP);
                } catch (NotEnoughBytesException e) {
                    e.printStackTrace();
                }
                return new RRsData(domain, type, classe, ttl, rdlenght, ipv4, (short) -1, vide, vide, vide,
                        vide, vide, -1, -1, -1, -1, vide, vide, questionSection);

            } else if (type == typeMX) {
                short preference = bytes.getShort();
                String exchange = readDomain(bytes, rdlenght);
                return new RRsData(domain, type, classe, ttl, rdlenght, null, preference, exchange, vide, vide, vide,
                        vide, -1, -1, -1, -1, vide, vide, questionSection);

            } else if (type == typeNS) {
                String nsdName = readDomain(bytes, rdlenght);
                return new RRsData(domain, type, classe, ttl, rdlenght, null, (short) -1, vide, nsdName, vide,
                        vide, vide, -1, -1, -1, -1, vide, vide, questionSection);

            } else if (type == typePTR) {
                String ptrName = readDomain(bytes, rdlenght);
                return new RRsData(domain, type, classe, ttl, rdlenght, null, (short) -1, vide, vide, ptrName,
                        vide, vide, -1, -1, -1, -1, vide, vide, questionSection);

            } else if (type == typeSOA) {
                String mName = readDomain(bytes, rdlenght);
                String rName = readDomain(bytes, rdlenght);
                int serial = bytes.getInt();
                int refresh = bytes.getInt();
                int retry = bytes.getInt();
                int expire = bytes.getInt();
                return new RRsData(domain, type, classe, ttl, rdlenght, null, (short) -1, vide, vide, vide,
                        mName, rName, serial, refresh, retry, expire, vide, vide, questionSection);

            } else if (type == typeCNAME) {
                String cName = readDomain(bytes, rdlenght);
                return new RRsData(domain, type, classe, ttl, rdlenght, null, (short) -1, vide, vide, vide,
                        vide, vide, -1, -1, -1, -1, vide, cName, questionSection);
            } else if (type == typeAAA) {
                String aaaaAdress = readAAAAAdress(bytes);
                return new RRsData(domain, type, classe, ttl, rdlenght, null, (short) -1, vide, vide, vide,
                        vide, vide, -1, -1, -1, -1, aaaaAdress, vide, questionSection);
            } else {
                return new RRsData(domain, type, classe, ttl, rdlenght, null, (short) -1, vide, vide, vide,
                        vide, vide, -1, -1, -1, -1, vide, vide, questionSection);
            }
        }
    }

    public static String readAAAAAdress(ByteBuffer bytes) {
        List<String> listAddress = new ArrayList<String>();
        for (int i = 0; i < (16 / 2); i++) {
            short partAdress = bytes.getShort();
            listAddress.add(String.valueOf(Short.toUnsignedInt(partAdress)));
        }
        String adress = assembleDomain(listAddress);
        return adress;

    }


    public static String readDomain(ByteBuffer bytes, int length) {
        int statingPositionReading = bytes.position();
        int endMaxReadingDomain = statingPositionReading + length - 1;

        String domain = "";

        byte ptr_or_label = bytes.get();
        byte test = (byte) (ptr_or_label >> 6 & 0b11);
        if (test == (byte) 3) {
            short offset = (short) ((ptr_or_label - 0xc0) << 8 | bytes.get() & 0xFF);
            //short offset = (short) (ptr_or_label << 8 | bytes.get() & 0xFF);
            //offset = (short) (offset & 0x3FFFF);
            int recoveryPosition = bytes.position();

            bytes.position(startingPosition + offset);
            String domain2 = readDomain(bytes, length);

            bytes.position(recoveryPosition);
            return domain2;

        } else {
            List<String> list2Domain = new ArrayList<String>();
            byte i = 0;
            byte partDomainLenght = ptr_or_label;
            byte nextByteToBeTreated = (byte) (i + 1);
            byte endPartDomain = (byte) (partDomainLenght + nextByteToBeTreated - 1);

            //recupere la position du pointeur de byte buffer ?? l'entr??e -> dans la fonction readDomain
            //determiner la longueur maximal  = la position du byte buffer + rdlenght -1;
            //a chaque incr??mentation du buffeur d??terminer si on depace cette position maximal
            while (partDomainLenght != 0x00 && bytes.position() <= endMaxReadingDomain) {
                byte[] hexToConvert = new byte[partDomainLenght];
                int j = 0;
                for (i = nextByteToBeTreated; i <= endPartDomain; i++) {
                    hexToConvert[j] = bytes.get();
                    j++;
                }

                String s = new String(hexToConvert, StandardCharsets.UTF_8);
                list2Domain.add(s);

                nextByteToBeTreated = (byte) (endPartDomain + 1);
                //definr si le procahin octet indique un offset
                partDomainLenght = bytes.get();
                byte test2 = (byte) (partDomainLenght >> 6 & 0b11);
                if (test2 == (byte) 3) {
                    short offset = (short) ((partDomainLenght - 0xc0) << 8 | bytes.get() & 0xFF);

                    //short offset = (short) (ptr_or_label << 8 | bytes.get() & 0xFF);
                    //offset = (short) (offset & 0x3FFFF);

                    int recoveryPosition = bytes.position();
                    bytes.position(startingPosition + offset);
                    domain = readDomain(bytes, length);

                    bytes.position(recoveryPosition);
                    //return domain;
                    break;
                }
                endPartDomain = (byte) (nextByteToBeTreated + partDomainLenght);
                nextByteToBeTreated++;

            }
            list2Domain.add(domain);
            return assembleDomain(list2Domain);
        }
    }

    public static String assembleDomain(List<String> listDomain) {
        String domain = "";
        if (!listDomain.isEmpty()) {
            domain = listDomain.get(0);
        }
        if (listDomain.size() >= 1) {
            for (String tmp : listDomain.subList(1, listDomain.size())) {
                domain = domain.concat("." + tmp);
            }
        }
        return domain;
    }


    public String toString() {
        String delimiter = " \n\t\t -> ";
        String prefix = "\t" + RRsData.class.getSimpleName() + " Type(" + type + ") Class(" + classe + "):\n";
        String sufix = "\n";

        if (type == typeA || type == typeMX || type == typeNS || type == typePTR ||
                type == typeSOA || type == typeAny || type == typeCNAME || type == typeAAA) {
            if (questionSection) {
                return new StringJoiner(delimiter, prefix, sufix)
                        .add(" \t\t -> Domain: " + domain)
                        .add("Type: " + type)
                        .add("Class: " + classe)
                        .toString();
            } else if (type == typeA) {
                return new StringJoiner(delimiter, prefix, sufix)
                        .add(" \t\t -> Domain: " + domain)
                        .add("Type A: " + type)
                        .add("Class: " + classe)
                        .add("TTL: " + ttl)
                        .add("Rdlength: " + rdlenght)
                        .add("Ipv4: " + ip.toString())
                        .toString();
            } else if (type == typeMX) {
                return new StringJoiner(delimiter, prefix, sufix)
                        .add(" \t\t -> Domain: " + domain)
                        .add("Type Mx: " + type)
                        .add("Class: " + classe)
                        .add("TTL: " + ttl)
                        .add("Rdlength: " + rdlenght)
                        .add("Preference: " + preference)
                        .add("Exchange: " + exchange)
                        .toString();
            } else if (type == typeNS) {
                return new StringJoiner(delimiter, prefix, sufix)
                        .add(" \t\t -> Domain: " + domain)
                        .add("Type NS: " + type)
                        .add("Class: " + classe)
                        .add("TTL: " + ttl)
                        .add("Rdlength: " + rdlenght)
                        .add("NSDName: " + nsdName)
                        .toString();
            } else if (type == typePTR) {
                return new StringJoiner(delimiter, prefix, sufix)
                        .add(" \t\t -> Domain: " + domain)
                        .add("Type PTR: " + type)
                        .add("Class: " + classe)
                        .add("TTL: " + ttl)
                        .add("Rdlength: " + rdlenght)
                        .add("PTRName: " + ptrName)
                        .toString();
            } else if (type == typeSOA) {
                return new StringJoiner(delimiter, prefix, sufix)
                        .add(" \t\t -> Domain: " + domain)
                        .add("Type SOA: " + type)
                        .add("Class: " + classe)
                        .add("TTL: " + ttl)
                        .add("Rdlength: " + rdlenght)
                        .add("MName: " + mName)
                        .add("RName: " + rName)
                        .add("Serial: " + serial)
                        .add("Refresh: " + refresh)
                        .add("Retry: " + retry)
                        .add("Eexpire: " + expire)
                        .toString();
            } else if (type == typeAny) {
                return new StringJoiner(delimiter, prefix, sufix)
                        .add(" \t\t -> Domain: " + domain)
                        .add("Type ANY: " + type)
                        .add("Class: " + classe)
                        .toString();
            } else if (type == typeCNAME) {
                return new StringJoiner(delimiter, prefix, sufix)
                        .add(" \t\t -> Domain: " + domain)
                        .add("Type CNAME: " + type)
                        .add("Class: " + classe)
                        .add("TTL: " + ttl)
                        .add("Rdlength: " + rdlenght)
                        .add("CNAM: " + cName)
                        .toString();

            } else {
                return new StringJoiner(delimiter, prefix, sufix)
                        .add(" \t\t -> Domain: " + domain)
                        .add("Type AAAA: " + type)
                        .add("Class: " + classe)
                        .add("TTL: " + ttl)
                        .add("Rdlength: " + rdlenght)
                        .add("AAAA Address: " + aaaaAdress)
                        .toString();
            }

        }
        return new StringJoiner(delimiter, prefix, sufix)
                .add(" \t\t -> Domain: " + domain)
                .add("Type: " + type)
                .add("Class: " + classe)
                .add("TTL: " + ttl)
                .add("Rdlength: " + rdlenght)
                .add("This type of data is not taken by the application, The parsing stopped")
                .toString();

    }

}
