import java.util.List;

public class Flags {
	public static int HEADER_LENTH = 16;
	public byte qr; 	// response (1 bit)
	public byte opcode; // (4 bits) 
	public byte aa;	 	// Authoritative Answer (1 bit)
	public byte tc; 	// Truncated (1 bit)
	public byte rd; 	// Recursion desired (1 bit) // fin 0
	public byte ra; 	// Recursion available (1 bit)
	public byte z;  	// (1 bit)
	public byte asa; 	// Answer authenticated (1 bit)
	public byte nad; 	// Non-authenticated data (1 bit)
	public byte rcode; 	// Reply code (4 bits) // fin 1
	
	//Constructeur 
	public Flags (byte qr, byte opcode, byte aa, byte tc, byte rd, byte ra, byte z, byte asa, 
			byte nad, byte rcode) {
		this.qr = qr;
		this.opcode = opcode;
		this.aa = aa;
		this.tc = tc;
		this.rd = rd;
		this.ra = ra;
		this.z  = z;
		this.asa = asa;
		this.nad = nad;
		this.rcode = rcode;
	}
	
	// Méthodes 
	public static Flags create(List<Byte> bytes) throws NotEnoughBytesException{
		if (bytes.size() < HEADER_LENTH) {
			throw NotEnoughBytesException(HEADER_LENTH , bytes.size());
		} 
		this.qr		= bytes.get(0) & 0x80;
		this.opcode 	= bytes.get(0) & 0x78;
		this.aa 	= bytes.get(0) & 0x04;
		this.tc 	= bytes.get(0) & 0x02;
		this.rd 	= bytes.get(0) & 0x01;
		this.ra 	= bytes.get(1) & 0x80;
		this.z  	= bytes.get(1) & 0x40;
		this.asa 	= bytes.get(1) & 0x20;
		this.nad 	= bytes.get(1) & 0x10;
		this.rcode 	= bytes.get(1) & 0x0F;	
		
		return new Flags(qr,opcode,aa,tc,rd,ra,z,asa,nad,rcode);
	}
	String delimeter = "\n";
	String prefix = Flags.class.getSimpleName() + " : \n ->";
	String sufix = "\n";
	
	
	public String reponse(byte qr) {
		switch(qr) {
			case 0 :
				return "Message is a query";
				break;
			case 1 :
				return "Message is an answer";
				break
			default :
				return "Unknown";
				break;
		}
	}
	public String opcode(byte opcode) {
		switch (opcode) {
			case 0 :
				return " Standard query (Query)" ;
				break ; 
			case 1 :
				return " Inverse request (IQuery)"; 
				break;
			case 2 : 
				return " Server status (Status)";
				break;
			case 3 : 
			case 4 : 
			case 5 :
			case 6 : 
			case 7 : 
			case 8 : 
			case 9 : 
			case 10 : 
			case 11 :
			case 12 : 
			case 13 : 
			case 14 :
			case 15 :  
				return " Reserved ";
				break;
			default : 
				return " Unknown";
				break;
		}
		
	}
	public String authoritativeAnswer(byte aa) {
		switch (aa) {
			case 0 : 
				return " Not Authoritative in the field ";
				break; 
			case 1 : 
				return " Authoritative in the field  ";
				break;
			default : 
				return " Unknown";
		
		}
	}
	public String truncated(byte tr) {
		switch (tr) {
		case 0 : 
			return " Message is not truncated ";
			break; 
		case 1 : 
			return " Message is truncated ";
			break;
		default : 
			return " Unknown";
	
		} 
	}
	public String recursionDesired(byte rd) {
		switch (rd) {
		case 0 : 
			return " Don't do query recurcively ";
			break; 
		case 1 : 
			return " Do query recursively ";
			break;
		default : 
			return " Unknown";
	
		}
	}
	public String recursionAvailable(byte ra) {
		switch (ra) {
		case 0 : 
			return " Recursive query accepted ";
			break; 
		case 1 : 
			return " Server can't do recursive queries ";
			break;
		default : 
			return " Unknown ";
	
		}
	}
	public String z(byte z) {
		switch (z) {
		case 0 : 
			return " Reseved ";
			break; 
		case 1 : 
			return " Not reserved ";
			break;
		default : 
			return " Unknown";
	
		} 
	}
	public String answerAuthentificated(byte asa) {
		switch (asa) {
		case 0 : 
			return "Answer was not authentificated by the server ";
			break; 
		case 1 : 
			return " Accepted ";
			break;
		default : 
			return " Unknown";
	
		}
	}
	public String nonAuthentificatedData(byte nad) {
		switch (nad) {
		case 0 : 
			return " Unacceptable";
			break; 
		case 1 : 
			return " Acceptable";
			break;
		default : 
			return " Unknown";
	
		} 
	}
	public String rcode(byte rcode) {
		switch (rcode) {
		case 0 : 
			return " No error (0) ";
			break; 
		case 1 : 
			return " Format error in the query (1) ";
			break;
		case 2 : 
			return " Problem on the server (2) ";
			break ; 
		case 3 : 
			return " The name does not exist (3) ";
			break ; 
		case 4 :
			return " Not implemented (4) ";
			break ;
		case 5  :
			return " Refus (5)";
			break ;
		case 6 : 
		case 7 : 
		case 8 : 
		case 9 : 
		case 10 : 
		case 11 :
		case 12 : 
		case 13 : 
		case 14 :
		case 15 : 
			return " Reserved (" + rcode + ") ";
			break;
		default : 
			return " Unknown";
			break ;
			
		}
	}
	
	String delimiter = " \n -> ";
	String prefix 	 = Flags.class.getSimpleName() + " :\n";
	String sufix	 = "\n";
	
	
	public String toString() {
		return new StringJoiner(delmiter, prefix , sufix)
				.add("Response  : " + this.reponse(qr))
				.add("Opcode 	: " + this.opcode(opcode))
				.add("Authoritative Answer : " +this.authoritativeAnswer(aa))
				.add("Truncated : " + this.truncated(tr))
				.add("Recursion desired    :" + this.recursionDesired(rd))
				.add("Recursion available  : " +this.recursionAvailable(ra))
				.add("Z : " + this.z(z))
				.add("Answer authenticated : " + this.answerAuthentificated(asa))
				.add("Non-authenticated data : " + this.nonAuthentificatedData(nad))
				.add("Reply code : " + this.rcode(rcode))
				.toString();
	}
}
