package tw.edu.nsysu.cse.iccsl.dnet;

import java.math.BigInteger;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class User {
	
	private static Pairing pairing		= null;
	@SuppressWarnings({ "unused", "rawtypes" })
	private static Field zr				= null;	
	
	//master public key
	private Element Delta				= null;				//\Delta				
	private Element[] vecH				= null;				//\vec{H}	
	private Element g 					= null;				//g
	private Element tiledA 				= null;				//e(g,g)^{alpha}
	private Element h_0 				= null;				//h_0
	private Element u_ap				= null;				//u^{\alpha'}
	private Element v_bp				= null;				//v^{\beta'}	
	private Element h;
	private Element[] sk 				= null;
	private Element alpha;
	private Element alphap;
	private Element betap;
	private Element delta;
	
	User(Element[] mpk, Element[] skk){
		
		pairing = PairingFactory.getPairing("curves/a.properties");
		//set mpk
  		Delta = mpk[6];
  		vecH = new Element[mpk.length-8];
  		for(int i=8; i<mpk.length; i++) {
  			vecH[i-8]=mpk[i];
  		}
  		
  		sk = skk;
	}

	public BigInteger decrypt(Element[] C, Element[] VectorY) {
		
		//D1
		Element tmp = sk[0].getImmutable();
		
		//D1 * K2^y2 ~ Kn^yn
		for (int i=0; i+2 < VectorY.length-1; i++) {
			tmp.mul( sk[i+2].getImmutable().powZn(VectorY[i+1]) );
		}
		//e(C1p * D1 * K2^y2 ~ Kn^yn)
		Element part_1 = pairing.pairing(C[0], tmp);
		//e(C2p, D2) * e(Delta, e1p, e2p)
		Element part_2 = pairing.pairing(C[1],sk[1]).mul(pairing.pairing(Delta, C[3].getImmutable().mul(C[4])) );
		Element Gamma = part_1.getImmutable().div(part_2);
		Element M = C[2].getImmutable().div(Gamma);
		
		
		return M.toBigInteger();
	}

}
