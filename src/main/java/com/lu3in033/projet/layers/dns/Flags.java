package com.lu3in033.projet.layers.dns;

import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.nio.ByteBuffer;
import java.util.StringJoiner;

public class Flags {
	public static int HEADER_LENTH = 2;
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

	public static Flags create(ByteBuffer bytes) throws NotEnoughBytesException {
		if (bytes.remaining() < HEADER_LENTH) {
			throw new NotEnoughBytesException(HEADER_LENTH ,bytes.remaining());
		}

		byte qr		= (byte) (bytes.get(0) >> 7 & 0x01);
		byte opcode = (byte) (bytes.get(0) >> 3 & 0x0F);
		byte aa 	= (byte) (bytes.get(0) >> 2 & 0x01);
		byte tc 	= (byte) (bytes.get(0) >> 1 & 0x01);
		byte rd 	= (byte) (bytes.get(0) >> 0 & 0x01);
		byte ra 	= (byte) (bytes.get(1) >> 7 & 0x01);
		byte z  	= (byte) (bytes.get(1) >> 6 & 0x01);
		byte asa 	= (byte) (bytes.get(1) >> 5 & 0x01);
		byte nad 	= (byte) (bytes.get(1) >> 4 & 0x01);
		byte rcode 	= (byte) (bytes.get(1) & 0x0F);

		return new Flags(qr,opcode,aa,tc,rd,ra,z,asa,nad,rcode);
	}

	
	public String reponse(byte qr) {
		return switch (qr) {
			case 0 -> "Message is a query";
			case 1 -> "Message is an answer";
			default -> "Unknown";
		};
	}
	public String opcode(byte opcode) {
		return switch (opcode) {
			case 0 -> " Standard query (Query 0)";
			case 1 -> " Inverse request (IQuery 1)";
			case 2 -> " Server status (Status 2)";
			case 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 -> " Reserved "+opcode;
			default -> " Unknown";
		};
	}
	public String authoritativeAnswer(byte aa) {
		return switch (aa) {
			case 0 -> " Not Authoritative in the field (0)";
			case 1 -> " Authoritative in the field  (1)";
			default -> " Unknown";
		};
	}
	public String truncated(byte tr) {
		return switch (tr) {
			case 0 -> " Message is not truncated (0)";
			case 1 -> " Message is truncated (1)";
			default -> " Unknown";
		};
	}
	public String recursionDesired(byte rd) {
		return switch (rd) {
			case 0 -> " Don't do query recursively (0)";
			case 1 -> " Do query recursively (1)";
			default -> " Unknown";
		};
	}
	public String recursionAvailable(byte ra) {
		return switch (ra) {
			case 0 -> " Recursive query accepted (0)";
			case 1 -> " Server can't do recursive queries (1)";
			default -> " Unknown ";
		};
	}
	public String z(byte z) {
		return switch (z) {
			case 0 -> " Reserved (0)";
			case 1 -> " Not reserved (1)";
			default -> " Unknown";
		};
	}
	public String answerAuthentificated(byte asa) {
		return switch (asa) {
			case 0 -> "Answer was not authentificated by the server (0)";
			case 1 -> " Accepted (1)";
			default -> " Unknown";
		};
	}
	public String nonAuthentificatedData(byte nad) {
		return switch (nad) {
			case 0 -> " Unacceptable (0)";
			case 1 -> " Acceptable (1) ";
			default -> " Unknown";
		};
	}
	public String rcode(byte rcode) {
		return switch (rcode) {
			case 0 -> " No error (0) ";
			case 1 -> " Format error in the query (1) ";
			case 2 -> " Problem on the server (2) ";
			case 3 -> " The name does not exist (3) ";
			case 4 -> " Not implemented (4) ";
			case 5 -> " Refus (5)";
			case 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 -> " Reserved (" + rcode + ") ";
			default -> " Unknown";
		};
	}
	
	public String delimiter = " \n -> ";
	public String prefix 	 = Flags.class.getSimpleName() + " :\n";
	public String sufix	 = "";
	
	
	public String toString() {
		return new StringJoiner(delimiter, prefix , sufix)
				.add(" -> Response : " + this.reponse(qr))
				.add("Opcode : " + this.opcode(opcode))
				.add("Authoritative Answer : " +this.authoritativeAnswer(aa))
				.add("Truncated	: " + this.truncated(tc))
				.add("Recursion desired	:" + this.recursionDesired(rd))
				.add("Recursion available : " +this.recursionAvailable(ra))
				.add("Z	: " + this.z(z))
				.add("Answer authenticated : " + this.answerAuthentificated(asa))
				.add("Non-authenticated data : " + this.nonAuthentificatedData(nad))
				.add("Reply code : " + this.rcode(rcode))
				.toString();
	}
}
