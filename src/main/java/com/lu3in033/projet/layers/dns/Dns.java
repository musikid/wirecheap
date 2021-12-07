package com.lu3in033.projet.layers.dns;

import java.util.*;
//recupérer les données
//analyser les données

public class Dns extends Layer{
	
	 //entête 
	 public static int HEADER_LENGHT = 12;
	 public short identifier; // 0,1
	 public Flags  flags;     // 2,3
	 public short questions;  // 4,5
	 public short answerRRs;  // 6,7
	 public short authorityRRs; // 8,9
	 public short additionalRRs; // 10,11
	 
	 //MESSAGE UTILE DNS 
	 //Question section 
	 public QuestSection questionSection;
	 //Answer section 
	 public AnSection answerSection;
	 //Authority section 
	 public AuthSection authoritySection; 
	 //Additional section 
	 public AdSection additionalSection;
	 
	 
	 //Constructeur 
	 public Dns( short identifier, Flags flags, short questions, short answerRRs, short  authorityRRs, 
			 short additionalRRs, QuestSection questionSection, AnSection answerSection, 
			 AuthSection authoritySection, AdSection additionalSection, List<Byte> playload ) {
		 super(playload);
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
	 public static Dns create(List<Byte> bytes) throws NotEnoughBytesException {
		 if (bytes.size() < HEADER_LENGTH) {
	            throw new NotEnoughBytesException(HEADER_LENGTH, bytes.size());
	        }
		 //recupérer tous les attributs 
		 //entête
		 short identifier 	 = LayerUtils.getShort(bytes, 0);
		 Flags flags 		 = Flags.create(bystes.sublist(2,3));
		 short questions	 = LayerUtils.getShort(bytes, 4);
		 short answerRRs 	 = LayerUtils.getShort(bytes, 6);
		 short authorityRRs  = LayerUtils.getShort(bytes, 8);
		 short additionalRRs = LayerUtils.getShort(bytes, 10);
		 
		 //Corp
		 //QuestSection questionSection 	= QuestSection.create(bystes.sublist(10, bytes.size()-1));	 
		 //AnSection answerSection 		= AnSection.create();
		 //AuthSection authoritySection  	= AuthSection.create();
		 //AdSection additionalSection 	= AdSection.create();
		 
		 //return Dns(identifier, flags, questions, answerRRs, authorityRRs, additionalRRs, 
				 //questionSection, answerSection, authoritySection, additionalSection );
		 
	 }
	 
	 
	 
	 // String
	 public String toString() {
		 return;
	 }
	 
}

