package projet;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class RRsData {
	public static int nextByte ;
	//attributs
	
	public String domain = ""; //OK 
	public short type ; 
	public int classe ; 
	
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
	

	
	//constructeur 
	public RRsData (String domain, short type, int classe, Ipv4Address ip, short preference,
			String exchange, String nsdName, String ptrName, String mName, String rName, 
			int serial, int refresh, int retry, int expire) {
		
		this.domain 	= domain;
		this.type 	= type;
		this.classe 	= classe;
		this.ip 	=ip;
		this.preference = preference;
		this.exchange 	= exchange;
		this.nsdName 	= nsdName;
		this.ptrName 	= ptrName;
		this.mName 	= mName;
		this.rName 	= rName;
		this.serial 	= serial; 
		this.refresh 	= refresh;
		this.retry 	= retry;
		this.expire 	= expire;
		
	}
	
	
	
	public static RRsData create(List<Byte> bytes) {
		//to recover the common values
		String domain 	= readDomain(bytes);
		short type 	  	= getShort(bytes, nextByte); nextByte+=2;
		int classe 	  	= getWord(bytes, nextByte); nextByte+=4;
		
		//type A
		switch (type) {
			case typeA :
				Ipv4Address ipv4 = Ipv4Address.create(bytes.subList(nextByte, nextByte+4));nextByte+=4;
				return new RRsData(domain,type, classe, ipv4, -1, "", "", "", "", "", -1,
						-1, -1, -1);
				break;
			
			case typeMX :
				short preference = getShort(bytes, nextByte); nextByte+=2;
				String exchange  = readDomain(bytes.subList(nextByte, bytes.size()));
				return new RRsData(domain,type, classe, null, preference, exchange, "", "", "", "", -1,
						-1, -1, -1);
				break;
			
			case typeNS : 
				String nsdName 	 = readDomain(bytes.subList(nextByte, bytes.size()));
				return new RRsData(domain,type, classe, null, -1, "", nsdName, "", "", "", -1,
						-1, -1, -1);
				break;
			
			case typePTR : 
				String ptrName 	 = readDomain(bytes.subList(nextByte, bytes.size()));
				return new RRsData(domain,type, classe, null, -1, "", "",ptrName, "", "", -1,
						-1, -1, -1);
				break;
				
			case typeSOA :
				String mName	 = readDomain(bytes.subList(nextByte, bytes.size()));
				String rName	 = readDomain(bytes.subList(nextByte, bytes.size()));
				int serial		 = getWord(bytes, nextByte); nextByte +=4;
				int refresh		 = getWord(bytes, nextByte); nextByte +=4;
				int retry		 = getWord(bytes, nextByte); nextByte +=4;
				int expire		 = getWord(bytes, nextByte); nextByte +=4;
				return new RRsData(domain,type, classe, null, -1, "", "", "", mName, rName, serial,
						refresh, retry, expire);
				break; 
			default : 
		}
		
	}
	
	public static short getShort(List<Byte> bytes, int i) {
        return (short) (bytes.get(i) << 8 | bytes.get(i + 1) & 0xFF);
    }
	
	public static int getWord(List<Byte> bytes, int i) {
        return (int) (bytes.get(i) << 8*3 | bytes.get(i) << 8*2| bytes.get(i) << 8| bytes.get(i + 1) & 0xFF);
    }
	
	
	public static String readDomain(List<Byte> bytes){
		
		List<String> list2Domain = new ArrayList<String>();
		byte i = 0;
		byte partDomainLenght = bytes.get(i);
		byte nextByteToBeTreated = (byte) (i+1);
		byte endPartDomain = (byte) (partDomainLenght + nextByteToBeTreated -1 );
		
		
		while(partDomainLenght != 0x00) {
			byte[] hexToConvert = new byte[partDomainLenght]; 
			int j = 0;
			for(i = nextByteToBeTreated; i<= endPartDomain; i++) {
				hexToConvert[j] = bytes.get(i);
				j++;
			}

			String s = new String(hexToConvert, StandardCharsets.UTF_8);
			list2Domain.add(s);
			
			nextByteToBeTreated = (byte) (endPartDomain  + 1);
			partDomainLenght 	= bytes.get(nextByteToBeTreated);
			endPartDomain 		= (byte) (nextByteToBeTreated + partDomainLenght );
			
			nextByteToBeTreated ++;
			
		}
		
		try {
			nextByte = nextByteToBeTreated ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	
	String delimiter = " \n -> ";
	String prefix 	 = RRsData.class.getSimpleName() + " " + type + " " + classe+ ":\n";
	String sufix	 = "\n";
	
	public String toString()
	{
		switch (type) {
		case typeA :
			return new StringJoiner(delimiter, prefix, sufix)
					.add("Domain : "+domain)
					.add("Type   : "+type)
					.add("Class  : "+classe)
					.add("Ipv4   : "+ip.toString())
					.toString();
			break;
		
		case typeMX :
			return new StringJoiner(delimiter, prefix, sufix)
					.add("Domain : "+domain)
					.add("Type   : "+type)
					.add("Class  : "+classe)
					.add("Preference   : "+preference)
					.add("Exchange	   : "+exchange)
					.toString();
			break;
		
		case typeNS : 
			return new StringJoiner(delimiter, prefix, sufix)
					.add("Domain : "+domain)
					.add("Type   : "+type)
					.add("Class  : "+classe)
					.add("NSDName   : "+nsdName)
					.toString();
			break;
		
		case typePTR : 
			return new StringJoiner(delimiter, prefix, sufix)
					.add("Domain : "+domain)
					.add("Type   : "+type)
					.add("Class  : "+classe)
					.add("PTRName   : "+ptrName)
					.toString();
			break;
			
		case typeSOA :
			return new StringJoiner(delimiter, prefix, sufix)
					.add("Domain : "+domain)
					.add("Type   : "+type)
					.add("Class  : "+classe)
					.add("MName  : "+mName)
					.add("RName  : "+rName)
					.add("Serial : "+serial)
					.add("Refresh: "+refresh)
					.add("Retry  : "+retry)
					.add("Eexpire: "+expire)
					.toString();
			break; 
		default : 
		}
	}
	
}
