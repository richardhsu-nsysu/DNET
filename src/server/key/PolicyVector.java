import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import java.util.Random;
import java.math.BigInteger;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.Base64;

public class PolicyVector {

	//pairing prameters
	private Field		mZr				= null;
	
	public PolicyVector (String curve_file_path) {
		mZr = PairingFactory.getPairing(curve_file_path).getZr();
	}

	public String genVector(String[] policy_set) {
		int len 		 = policy_set.length/3;
		Element[] vector = new Element[len*4];
		Element a = mZr.newElement();
		Element b = mZr.newElement();
		Element c = mZr.newElement();
		Element one = mZr.newElement().set(1);
		for(int i=0; i<len; i++){
			int k = i*3;
			a.set(new BigInteger(policy_set[k].getBytes())); 
			b.set(new BigInteger(policy_set[k+1].getBytes()));
			c.set(new BigInteger(policy_set[k+2].getBytes()));
			int j = i*4;
			Element ab = a.getImmutable().mul(b.getImmutable());
			Element ac = a.getImmutable().mul(c.getImmutable());
			Element bc = b.getImmutable().mul(c.getImmutable());
			Element abc = ab.mul(c.getImmutable());
			vector[j] 	= abc.negate();
			vector[j+1] = ab.add(ac).add(bc);
			vector[j+2] = a.getImmutable().add(b.getImmutable()).add(c.getImmutable()).negate();
			vector[j+3] = one;
		}
		String V = "";
		for(int j=0; j<len*4; j++){
			V = V + Base64.getEncoder().encodeToString(vector[j].toBytes()) + "\n";
		}
		return V;
	}

	public static void writeToFile(String filename,String content) {
		System.out.println("writing to " + filename + " ...");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			bw.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main (String[] args) {
		System.out.println("----------------------");
		System.out.println("start generate policy vector ...");
		System.out.println("----------------------");
		PolicyVector policyVector = new PolicyVector("curves/a.properties");
		String pv = policyVector.genVector(args);
		writeToFile("PolicyVector.txt",pv);
	}
}