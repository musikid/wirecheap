package com.lu3in033.projet.layers.dns;

import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.nio.ByteBuffer;

public class TestDns {
    public static void main(String[] args)  {

        //Test the functions of the Flags class
            ByteBuffer bb = ByteBuffer.allocate(16);
            bb.put((byte) 0x99);
            bb.put((byte) 0x99);
            bb.rewind();

            try {//successful
                //Test of the function create
                Flags flag = Flags.create(bb);
                //Test of the function toString
                System.out.println(flag);
            } catch (NotEnoughBytesException e) {
                e.printStackTrace();
            }

        // RRsData
            ByteBuffer bb2 = ByteBuffer.allocate(100);
            //domain : Nacer.Domain
            bb2.put((byte) 0x05);
            bb2.put((byte) 0x4e);
            bb2.put((byte) 0x61);
            bb2.put((byte) 0x63);
            bb2.put((byte) 0x65);
            bb2.put((byte) 0x72);
            bb2.put((byte) 0x06);
            bb2.put((byte) 0x44);
            bb2.put((byte) 0x6f);
            bb2.put((byte) 0x6d);
            bb2.put((byte) 0x61);
            bb2.put((byte) 0x69);
            bb2.put((byte) 0x6e);
            bb2.put((byte) 0x00);
            //type ANY
            bb2.put((byte) 0X00);
            bb2.put((byte) 0xff);
            //class
            bb2.put((byte) 0X00);
            bb2.put((byte) 0x01);
            bb2.rewind();

            //Test of function readDomain
                //String result;
                //result = RRsData.readDomain(bb2);
                //System.out.println(result);
                //Test the functions of the RRsData class
                //bb2.rewind();

            //Test of the function readDomain
                //RRsData rRsData = RRsData.create(bb2, true);
                //Test of the function assembleDomain
                //Test of the function toString
                //System.out.println(rRsData);
                //bb2.rewind();




        //Create a dns parser
            ByteBuffer bb3 = ByteBuffer.allocate(100);
            //id
            bb3.put((byte) 0x00); bb3.put((byte) 0xff);
            //flag
            bb3.put((byte) 0x99); bb3.put((byte) 0x99);
            //qcount
            bb3.put((byte) 0x00); bb3.put((byte) 0x01);
            //ancount
            bb3.put((byte) 0x00); bb3.put((byte) 0x00);
            //nscount
            bb3.put((byte) 0x00); bb3.put((byte) 0x00);
            //arcount
            bb3.put((byte) 0x00); bb3.put((byte) 0x00);
            //domain : Nacer.Domain
            bb3.put((byte) 0x05);
            bb3.put((byte) 0x4e);
            bb3.put((byte) 0x61);
            bb3.put((byte) 0x63);
            bb3.put((byte) 0x65);
            bb3.put((byte) 0x72);
            bb3.put((byte) 0x06);
            bb3.put((byte) 0x44);
            bb3.put((byte) 0x6f);
            bb3.put((byte) 0x6d);
            bb3.put((byte) 0x61);
            bb3.put((byte) 0x69);
            bb3.put((byte) 0x6e);
            bb3.put((byte) 0x00);
            //type ANY
            bb3.put((byte) 0X00);
            bb3.put((byte) 0xff);
            //class
            bb3.put((byte) 0X00);
            bb3.put((byte) 0x01);
            bb3.rewind();
            //Test of the function create
        try {
            Dns dnsParser = Dns.create(bb3);
            System.out.println(dnsParser);
        } catch (Exception e) {
            e.printStackTrace();

        }

/*
            try {
                    short identifier 	 = bb3.getShort();
                    ByteBuffer raw = ByteBuffer.allocate(2);
                    bb3.get(raw.array());
                    Flags flags 		 = Flags.create(raw);
                    short questions	     = bb3.getShort();
                    short answerRRs 	 = bb3.getShort();
                    short authorityRRs   = bb3.getShort();
                    short additionalRRs  = bb3.getShort();
                    System.out.println(identifier);
                    System.out.println(flags);
                    System.out.println(questions);
                    System.out.println(answerRRs);
                    System.out.println(authorityRRs);
                    System.out.println(additionalRRs);

                    List<RRsData> questionSection = new ArrayList<RRsData>();
                    boolean questionSectionBool = false;
                    if (questions >= 1){questionSectionBool= true; }
                    for (int i = 0; i<questions; i++){
                            RRsData data = RRsData.create(bb3,true);
                            questionSection.add(data);

                   }

                    List<RRsData>  answerSection = new ArrayList<RRsData>();
                    for (int i = 0; i<answerRRs; i++){
                            RRsData data = RRsData.create(bb3,false);
                            answerSection.add(data);
                    }

                    List<RRsData>  authoritySection = new ArrayList<RRsData>();
                    for (int i = 0; i<authorityRRs; i++){
                            RRsData data = RRsData.create(bb3,false);
                            authoritySection.add(data);
                    }

                    List<RRsData> additionalSection = new ArrayList<RRsData>();
                    for (int i = 0; i<additionalRRs; i++){
                            RRsData data = RRsData.create(bb3,false);
                            additionalSection.add(data);
                    }

                    Dns dns = new Dns(identifier, flags, questions, answerRRs, authorityRRs, additionalRRs,
                            questionSection, answerSection, authoritySection, additionalSection );
                    System.out.println(dns);

            } catch (NotEnoughBytesException e) {
                    e.printStackTrace();
            }*/

        //Test of the function toString
    }

}
