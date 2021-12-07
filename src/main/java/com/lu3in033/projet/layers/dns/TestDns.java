
import java.nio.charset.StandardCharsets;
import java.util.List;


public class TestDns {
	public int nextByte;
	
	public List<String> readDomain(List<Byte> bytes){
		
		List<String> list2Domain = new ArrayList<String>();
		byte i = 0b0;
		byte partDomainLenght = bytes.get(i);
		byte endPartDomain = partDomainLenght +i;
		byte nextByteToBeTreated = i+0b01;
		
		while(partDomainLenght != 0x00) {
			for(i = nextByteToBeTreated; i< endPartDomain; i++) {
				byte hexToConvert = bytes.get(i);
				//transformation 
				String s = new String(hexToConvert, StandardCharsets.UTF_8);
				//ajout à la liste 
				list2Domain.add(s);
			}
			nextByteToBeTreated = i+0b1;
			partDomainLenght 	= bytes.get(nextByteToBeTreated);
			endPartDomain 		= nextByteToBeTreated + partDomainLenght;
		}
		//À la sortie de la boucle on s'arrete sur le mot de fin 0x00
		nextByte = nextByteToBeTreated +1 ;
		return list2Domain;
	}
	
	
	public static void main(String[] args) {
		//String example = "1234";
        // string to byte[]
        //byte[] bytes = example.getBytes();
		
        //System.out.println(bytes[0]);
		
		//creer la liste de byte
		List<Byte> bytes = new ArrayList<Byte>();
		bytes.add(0x05, 0x4e, 0x61, 0x63, 0x65, 0x72,0x00); // means Nacer 
		
		//creer la list de string 
		List<String> listDomain = new ArrayList<String>();
		listDomain = readDomain(bytes);
		System.out.println(listDomain);
		
		
	}
}
