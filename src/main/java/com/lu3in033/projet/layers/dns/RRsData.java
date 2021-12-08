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

	public boolean questionSection;
	//Question section
	public String domain ; //OK
	public short type ; 
	public short classe ;
	//Other sections
	public int ttl;
	public short rdlenght;
	
	//A 
	public static short typeA = 0x0001;
	public Ipv4Address ip	; 
	
	//MX
	public static short typeMX = 0x000F;
	public short preference ;
	public String exchange 	; 	//OK
	
	//NS
	public static short typeNS = 0x0002;
	public String nsdName ; 	//OK
	
	//PTR
	public static short typePTR = 0x000C;
	public String ptrName ; 	//OK
	
	//SOA
	public static short typeSOA = 0x0006;
	public String mName ; 		//OK
	public String rName ;		//OK
	public int serial 	;
	public int refresh 	;
	public int retry 	;
	public int expire 	;

	public static short typeAny = 0x00ff;
	

	
	//constructeur 
	public RRsData (String domain, short type, short classe,int ttl, short rdlenght, Ipv4Address ip, short preference,
			String exchange, String nsdName, String ptrName, String mName, String rName, 
			int serial, int refresh, int retry, int expire, boolean questionSection) {
		
		this.domain 	= domain;
		this.type 		= type;
		this.classe 	= classe;
		this.ttl		= ttl;
		this.rdlenght	= rdlenght;
		this.ip 		= ip;
		this.preference = preference;
		this.exchange 	= exchange;
		this.nsdName 	= nsdName;
		this.ptrName 	= ptrName;
		this.mName 		= mName;
		this.rName 		= rName;
		this.serial 	= serial; 
		this.refresh 	= refresh;
		this.retry 		= retry;
		this.expire 	= expire;
		this.questionSection = questionSection;
		
	}


	public static RRsData create(ByteBuffer bytes, boolean questionSection) {
		//to recover the common values
		String domain 	= readDomain(bytes);
		short type 	  	= bytes.getShort();
		short classe 	= bytes.getShort();


		String vide = "";
		if (questionSection == true){
			return new RRsData(domain, type, classe,-1, (short)-1, null, (short) -1, vide, vide, vide,
					vide, vide, -1, -1, -1, -1, questionSection);

		} else {
			int ttl = bytes.getInt();
			short rdlenght =   bytes.getShort();

			if (type == typeA) {
				ByteBuffer rawIP = ByteBuffer.allocate(4);
				bytes.get(rawIP.array());
				Ipv4Address ipv4 = null;
				try {
					ipv4 = Ipv4Address.create(rawIP);
				} catch (NotEnoughBytesException e) {
					e.printStackTrace();
				}
				return new RRsData(domain, type, classe,ttl,rdlenght, ipv4, (short) -1, vide, vide, vide,
						vide, vide, -1, -1, -1, -1,questionSection);

			} else if (type == typeMX) {
				short preference = bytes.getShort();
				String exchange = readDomain(bytes);
				return new RRsData(domain, type, classe,ttl,rdlenght, null, preference, exchange, vide, vide, vide,
						vide, -1, -1, -1, -1,questionSection);

			} else if (type == typeNS) {
				String nsdName = readDomain(bytes);
				return new RRsData(domain, type, classe,ttl,rdlenght, null, (short) -1, vide, nsdName, vide,
						vide, vide, -1, -1, -1, -1,questionSection);

			} else if (type == typePTR) {
				String ptrName = readDomain(bytes);
				return new RRsData(domain, type, classe,ttl,rdlenght, null, (short) -1, vide, vide, ptrName,
						vide, vide, -1, -1, -1, -1,questionSection);

			} else if (type == typeSOA) {
				String mName = readDomain(bytes);
				String rName = readDomain(bytes);
				int serial = bytes.getInt();
				int refresh = bytes.getInt();
				int retry = bytes.getInt();
				int expire = bytes.getInt();
				return new RRsData(domain, type, classe,ttl,rdlenght, null, (short) -1, vide, vide, vide,
						mName, rName, serial, refresh, retry, expire,questionSection);

			} else {
				return new RRsData(domain, type, classe,ttl, rdlenght, null, (short) -1, vide, vide, vide,
						vide, vide, -1, -1, -1, -1,questionSection);
			}
		}

	}

	public static String readDomain(ByteBuffer bytes){
		
		List<String> list2Domain = new ArrayList<String>();
		byte i = 0;
		byte partDomainLenght = bytes.get();
		byte nextByteToBeTreated = (byte) (i+1);
		byte endPartDomain = (byte) (partDomainLenght + nextByteToBeTreated -1 );
		
		
		while(partDomainLenght != 0x00) {
			byte[] hexToConvert = new byte[partDomainLenght]; 
			int j = 0;
			for(i = nextByteToBeTreated; i<= endPartDomain; i++) {
				hexToConvert[j] = bytes.get();
				j++;
			}

			String s = new String(hexToConvert, StandardCharsets.UTF_8);
			list2Domain.add(s);
			
			nextByteToBeTreated = (byte) (endPartDomain  + 1);
			partDomainLenght 	= bytes.get();
			endPartDomain 		= (byte) (nextByteToBeTreated + partDomainLenght );
			
			nextByteToBeTreated ++;
			
		}
		return assembleDomain(list2Domain);
	}
	
	public static  String assembleDomain(List<String> listDomain) {
		String domain = listDomain.get(0);
		for (String tmp : listDomain.subList(1, listDomain.size()) ) {
			domain  = domain.concat("." + tmp);
		}
		return domain;
	}
	
	

	
	public String toString() {
		String delimiter = " \n -> ";
		String prefix 	 = "   "+RRsData.class.getSimpleName() + " Type(" + type + ") Class(" + classe+ ") :\n";
		String sufix	 = "\n";
		if (questionSection == true) {
			return new StringJoiner(delimiter, prefix, sufix)
					.add(" -> Domain : " + domain)
					.add("Type   : " + type)
					.add("Class  : " + classe)
					.toString();
		} else {
			if (type == typeA) {
				return new StringJoiner(delimiter, prefix, sufix)
						.add(" -> Domain : " + domain)
						.add("Type   : " + type)
						.add("Class  : " + classe)
						.add("TTL	 : " + ttl)
						.add("Rdlength : " + rdlenght)
						.add("Ipv4   : " + ip.toString())
						.toString();
			} else if (type == typeMX) {
				return new StringJoiner(delimiter, prefix, sufix)
						.add(" -> Domain : " + domain)
						.add("Type   : " + type)
						.add("Class  : " + classe)
						.add("TTL	 : " + ttl)
						.add("Rdlength : " + rdlenght)
						.add("Preference   : " + preference)
						.add("Exchange	   : " + exchange)
						.toString();
			} else if (type == typeNS) {
				return new StringJoiner(delimiter, prefix, sufix)
						.add(" -> Domain : " + domain)
						.add("Type   : " + type)
						.add("Class  : " + classe)
						.add("TTL	 : " + ttl)
						.add("Rdlength : " + rdlenght)
						.add("NSDName   : " + nsdName)
						.toString();
			} else if (type == typePTR) {
				return new StringJoiner(delimiter, prefix, sufix)
						.add(" -> Domain : " + domain)
						.add("Type   : " + type)
						.add("Class  : " + classe)
						.add("TTL	 : " + ttl)
						.add("Rdlength : " + rdlenght)
						.add("PTRName   : " + ptrName)
						.toString();
			} else if (type == typeSOA) {
				return new StringJoiner(delimiter, prefix, sufix)
						.add(" -> Domain : " + domain)
						.add("Type   : " + type)
						.add("Class  : " + classe)
						.add("TTL	 : " + ttl)
						.add("Rdlength : " + rdlenght)
						.add("MName  : " + mName)
						.add("RName  : " + rName)
						.add("Serial : " + serial)
						.add("Refresh: " + refresh)
						.add("Retry  : " + retry)
						.add("Eexpire: " + expire)
						.toString();
			} else if (type == typeAny) {
				return new StringJoiner(delimiter, prefix, sufix)
						.add(" -> Domain : " + domain)
						.add("Type   : " + type)
						.add("Class  : " + classe)
						.toString();
			}
			return new StringJoiner(delimiter, prefix, sufix)
					.add(" -> Domain : " + domain)
					.add("Type   : " + type)
					.add("Class  : " + classe)
					.add("TTL	 : " + ttl)
					.add("Rdlength : " + rdlenght)
					.add("this type of data is not taken by the application")
					.toString();
		}
	}
	
}
