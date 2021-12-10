package com.lu3in033.projet.layers.dns;

import com.lu3in033.projet.layers.NotEnoughBytesException;

import java.nio.ByteBuffer;
import java.util.*;
//recupérer les données
//analyser les données

public class Dns {
	 //entête 
	 public static int HEADER_LENGHT = 12;
	 public short identifier; // 0,1
	 public Flags  flags ;     // 2,3
	 public short questions;  // 4,5
	 public short answerRRs;  // 6,7
	 public short authorityRRs; // 8,9
	 public short additionalRRs; // 10,11

	public static boolean stopParsing = false;
	 
	 //MESSAGE UTILE DNS 
	 //Question section 
	 public List<RRsData> questionSection;
	 //Answer section 
	 public List<RRsData> answerSection;
	 //Authority section 
	 public List<RRsData> authoritySection;
	 //Additional section 
	 public List<RRsData> additionalSection;
	 
	 
	 //Constructeur 
	 public Dns( short identifier, Flags flags, short questions, short answerRRs, short  authorityRRs, 
			 short additionalRRs, List<RRsData> questionSection, List<RRsData> answerSection,
				 List<RRsData> authoritySection, List<RRsData> additionalSection ) {
		 this.identifier = identifier;
		 this.flags = flags;
		 this.questions = questions;
		 this.answerRRs = answerRRs;
		 this.authorityRRs = authorityRRs;
		 this.additionalRRs = additionalRRs;
		 
		 this.questionSection = questionSection;
		 this.answerSection = answerSection;
		 this.authoritySection = authoritySection;
		 this.additionalSection = additionalSection;
	 }
	 
	 
	 //Méthode 
	 public static Dns create(ByteBuffer bytes) throws NotEnoughBytesException {

		 if (bytes.remaining() < HEADER_LENGHT) {
	            throw new NotEnoughBytesException(HEADER_LENGHT, bytes.remaining());
	        }

		 RRsData.startingPosition = bytes.position();


		 //recupérer tous les attributs 
		 //entête
		 short identifier 	 = bytes.getShort();
		 ByteBuffer raw = ByteBuffer.allocate(2);
		 bytes.get(raw.array());
		 Flags flags 		  = Flags.create(raw);
		 short questions	  = bytes.getShort();
		 short answerRRs 	  = bytes.getShort();
		 short authorityRRs   = bytes.getShort();
		 short additionalRRs  = bytes.getShort();

		 //RRsData.startingPosition = bytes.position();
		 List<RRsData> questionSection = new ArrayList<RRsData>();
		 boolean questionSectionBool = false;
		 if (questions >= 1){questionSectionBool= true;}
		 for (int i = 0; i<questions; i++){
			 if (stopParsing) {
				 break;
			 }
				 RRsData data = RRsData.create(bytes, true);
				 questionSection.add(data);
		 }

		 List<RRsData>  answerSection = new ArrayList<RRsData>();
		 for (int i = 0; i<answerRRs; i++){
			 if (stopParsing) {
				 break;
			 }
			 RRsData data = RRsData.create(bytes, false);
			 answerSection.add(data);
		 }

		 List<RRsData>  authoritySection = new ArrayList<RRsData>();
		 for (int i = 0; i<authorityRRs; i++){
			 if (stopParsing) {
				 break;
			 }
			 RRsData data = RRsData.create(bytes, false);
			 authoritySection.add(data);
		 }

		 List<RRsData> additionalSection = new ArrayList<RRsData>();
		 for (int i = 0; i<additionalRRs; i++){
			 if(stopParsing) {
				 break;
			 }
			 RRsData data = RRsData.create(bytes, false);
			 additionalSection.add(data);
		 }

		 return  new Dns(identifier, flags, questions, answerRRs, authorityRRs, additionalRRs,
				 questionSection, answerSection, authoritySection, additionalSection );
	 }

	 public String assemblerSection(List<RRsData> dataList){
		 String assembler = "";
		 for(RRsData tmp : dataList){
			 assembler = assembler.concat(tmp.toString().replaceAll("\n", "\n    "));
		 }
		 return assembler;
	 }
	 // String
	 String delimiter = " \n\t -> ";
	 String prefix 	 = Dns.class.getSimpleName() + " :\n";
	 String sufix	 = "\n";
	 public String toString() {
		 return new StringJoiner(delimiter, prefix, sufix)
				 .add(" \t -> Identifier : " + identifier)
				 .add( flags.toString().replaceAll("\n", "\n    "))
				 .add("Question : " + questions)
				 .add("AnswerRRs: " + answerRRs)
				 .add("AuthorityRRs : " + authorityRRs +"\n")
				 .add("Question Section : \n" + assemblerSection(questionSection))
				 .add("Answer Section 	: \n" + assemblerSection(answerSection))
				 .add("Authority Section : \n" + assemblerSection(authoritySection))
				 .add("Additional Section :\n" + assemblerSection(additionalSection))
				 .toString();
	 }
}

