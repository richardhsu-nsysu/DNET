package tw.edu.nsysu.cse.iccsl.dnet;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class EdgeDevice {
	
	private static Pairing pairing		= null;
	@SuppressWarnings("rawtypes")
	private static Field zr				= null;	
	
	//master public key
	private Element g 					= null;				//g
	private Element tiledA 				= null;				//e(g,g)^{alpha}
	private Element h_0 				= null;				//h_0
	private Element u_ap				= null;				//u^{\alpha'}
	private Element v_bp				= null;				//v^{\beta'}		
	private Element[] vecH				= null;				//\vec{H}	
	
	//cooperative secret key					
	private Element xp					= null;				//x'
	private Element	yp					= null;				//y'
	
	EdgeDevice(Element[] mpk, Element[] csk){
		pairing = PairingFactory.getPairing("curves/a.properties");
		zr = pairing.getZr();
		//set mpk
  		g = mpk[0];
  		tiledA = mpk[1];
  		h_0 = mpk[3];
  		u_ap = mpk[4];
  		v_bp = mpk[5];
  		vecH = new Element[mpk.length-7];
  		for(int i=7; i<mpk.length; i++) {
  			vecH[i-7]=mpk[i];
  		}
  		//set csk
  		xp = csk[0];
  		yp = csk[1];
	}
	
	public Element[] coEncrypt(Element[] vectorY, Element[] PC) {
		Element sp = zr.newRandomElement();
		Element[] C = new Element[5];
		C[0] = PC[0].getImmutable().mul( g.getImmutable().powZn(sp));
		//(h_0xh_1^{y_1}x...xh_n^{y_n})^s'
		Element tmp = h_0.getImmutable();
		for (int i=0;i<vectorY.length;i++) {
			tmp.mul( vecH[i].getImmutable().powZn(vectorY[i]) );
		}
		C[1] = PC[1].getImmutable().mul( tmp.getImmutable().powZn(sp) );
		C[2] = PC[2].getImmutable().mul( tiledA.getImmutable().powZn(sp));
		C[3] = PC[3].getImmutable().mul( u_ap.getImmutable().powZn(sp)).powZn(xp);
		C[4] = PC[4].getImmutable().mul( v_bp.getImmutable().powZn(sp)).powZn(yp);
		
		return C;
	}


}
